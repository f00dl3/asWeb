/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 16 Apr 2018
 */

function actOnNoteRequest() {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    getNoteFromDatabase(thisFormData);
}

function getDatabasedNotes() {
    
}

function getNoteFromDatabase() {
    
}

function getMySqlBasicData() {
    
}

function getMySqlTableData() {
    
}

function populateDatabasedNotes(dbNotes) {
    var cols = [ "File", "Date", "About" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>"
    dbNotes.forEach(function (note) {
        rData += "<form id='NoteForm' class='tr'>" +
                "<span class='td'><input type='checkbox' class='ClickMe' name='DoNote' value='Yes' /> " + note.TextFileName + "</span>" +
                "<input type='hidden' name='TextFileName' value='" + note.TextFileName + "'/>" +
                "<span class='td'>" + note.Date + "</span>" +
                "<span class='td'>" + note.Topic + "</span>" +
                "</form>";
    });
    rData += "</div>";
    dojo.byId("InfoHolder").innerHTML = rData;
    dojo.query(".ClickMe").connect("onchange", actOnNoteRequest);
}

function populateMySqlBasicData(myInfo) {
    var cols = [ "Database", "Size (b)", "Rows" ];
    var rData = "<h3>MySQL Details</h3>" +
            "<button id='ShowNotes' class='UButton'>Notes</button>" +
            "<button id='ShowDetail' class='UButton'>Detail</button>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    myInfo.forEach(function (my) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + my.Database + "</span>" +
                "<span class='td'>" + autoUnits(my.DBSize) + "</span>" +
                "<span class='td'>" + autoUnits(my.DBRows) + "</span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId("InfoHolder").innerHTML = rData;
    var noteButton = dojo.byId("ShowNotes");
    var fullInfoButton = dojo.byId("ShowFull");
    dojo.connect(noteButton, "click", getDatabasedNotes);
    dojo.connect(fullInfoButton, "click", getMySqlTableData);
}

function populateMySqlTableData(myFullInfo) {
    var cols = [ "Database", "Table", "Size", "Rows" ];
    var rData = "<h3>MySQL Table Detail Estimates</h3>" +
            "<button id='ShowNotes' class='UButton'>Notes</button>" +
            "<button id='ShowBrief' class='UButton'>Basic</button>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    myFullInfo.forEach(function (myFull) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + myFull.Database + "</span>" +
                "<span class='td'>" + myFull.Table + "</span>" +
                "<span class='td'>" + myFull.Size + "</span>" +
                "<span class='td'>" + myFull.Rows + "</span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId("InfoHodler").innerHTML = rData;
    var noteButton = dojo.byId("ShowNotes");
    var basicInfoButton = dojo.byId("ShowBasic");
    dojo.connect(noteButton, "click", getDatabasedNotes);
    dojo.connect(basicInfoButton, "click", getMySqlBasicData);
}