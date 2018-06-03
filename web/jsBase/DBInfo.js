/* 
by Anthony Stump
Created: 3 Jun 2018
 */

function actOnShowInfo(event) {
    dojo.stopEvent(event);
    getDatabaseOverview();
}

function actOnShowNotes(event) {
    dojo.stopEvent(event);
    getDatabaseNotes();
}

function actOnShowThisNote(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    getDatabaseNotes(thisFormData.FileName)
}

function getDatabaseNotes(noteToFind) {
    aniPreload("on");
    var noteSearchString = "%";
    if(isSet(noteToFind)) {
        noteSearchString = noteToFind;
    }
    var thePostData = {
        "doWhat": "Notes",
        "noteSearchString": noteSearchString
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Logs"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    if(isSet(noteToFind)) {
                        generateThisNote(data);
                    } else {
                        generateDatabaseNoteTable(data);
                    }
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for DBNotes FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                }
            );
    });
}

function getDatabaseOverview() {
    aniPreload("on");
    var thePostData = { "doWhat": "getDbInfo" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("DBInfo"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    generateDatabaseOverview(data.dbInfo);
                    generateDatabaseOverviewByTable(data.dbInfoByTable);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for DBInfo FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                }
            );
    });
}

function generateDatabaseNoteTable(noteData) {
    $("#dbOverviewHolder").hide();
    $("#dbNoteHolder").show();
    var tCols = [ "Show", "File", "Date", "Topic" ];
    var rData = "<button class='UButton' id='ShowInfo'>Return</button><p>" +
            "<div class='table'><div class='tr'>";;
    for(var i = 0; i < tCols.length; i++) { rData += "<span class='td'><strong>" + tCols[i] + "</strong></span>"; }
    rData += "</div>";
    noteData.forEach(function(notes) {
        rData += "<form class='tr'>" +
                "<span class='td'><input type='checkbox' value='showNote' class='noteChecker'/></span>" +
                "<span class='td'><input type='hidden' name='FileName' value='" + notes.TextFileName + "'/>" + notes.TextFileName + "</span>" +
                "<span class='td'>" + notes.Date + "</span>" +
                "<span class='td'>" + notes.Topic + "</span>" +
                "</form>";
    });
    rData += "</div>";
    dojo.byId("dbNoteHolder").innerHTML = rData;
    var showInfo = dojo.byId("ShowInfo");
    dojo.connect(showInfo, "click", actOnShowInfo);
    dojo.query(".noteChecker").connect("onchange", actOnShowThisNote);
}

function generateDatabaseOverview(myInfo) {
    $("#thisDbNote").hide();
    $("#dbNoteHolder").hide();
    $("#dbOverviewHolder").show();
    var tCols = [ "Database", "Size (b)", "Rows" ];
    var rData = "<table><tr>";
    for(var i = 0; i < tCols.length; i++) { rData += "<th>" + tCols[i] + "</th>"; }
    rData += "</tr>";
    myInfo.forEach(function (dbInfo) {
        rData += "<tr>" +
                "<td>" + dbInfo.Database + "</td>" +
                "<td>" + autoUnits(dbInfo.DBSize) + "</td>" +
                "<td>" + autoUnits(dbInfo.DBRows) + "</td>" +
                "</tr>";
    });
    rData += "</table>";
    dojo.byId("dbOverview").innerHTML = rData;
}

function generateDatabaseOverviewByTable(myTables) {
    var tCols = [ "Database", "Table", "Size", "Rows" ];
    var rData = "<table><tr>";
    for(var i = 0; i < tCols.length; i++) { rData += "<th>" + tCols[i] + "</th>"; }
    rData += "</tr>";
    myTables.forEach(function (dbInfo2) {
        rData += "<tr>" +
                "<td>" + dbInfo2.Database + "</td>" +
                "<td>" + dbInfo2.Table + "</td>" +
                "<td>" + autoUnits(dbInfo2.Size) + "</td>" +
                "<td>" + autoUnits(dbInfo2.Rows) + "</td>" +
                "</tr>";
    });
    rData += "</table>";
    dojo.byId("dbOverviewByTable").innerHTML = rData;
}

function generateThisNote(noteData) {
    $("#dbNoteHolder").hide();
    $("#thisDbNote").show();
    var rData = "<button class='UButton' id='CloseNote'>Close</button><p>";
    noteData.forEach(function (note) {
        rData += "<h3>" + note.TextFileName + "</h3>" +
                note.Note;
    });
    dojo.byId("thisDbNote").innerHTML = rData;
    var closeButton = dojo.byId("CloseNote");
    dojo.connect(closeButton, "click", actOnShowNotes);
}

function layoutDatabaseOverview() {
    var layout = "<button class='UButton' id='ShowNotes'>Show Notes</button><p>" +
            "<div id='dbOverview'></div><p>" +
            "<div id='dbOverviewByTable'></div>";
    dojo.byId("dbOverviewHolder").innerHTML = layout;
    getDatabaseOverview();
    var showNoteButton = dojo.byId("ShowNotes");
    dojo.connect(showNoteButton, "click", actOnShowNotes);
}

function init() {
    layoutDatabaseOverview();
}

dojo.ready(init);