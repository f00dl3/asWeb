/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 13 May 2019
*/

function actOnCalendarSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(isSet(thisFormData.QuickStart) && isSet(thisFormData.QuickStartTime) && isSet(thisFormData.QuickTitle)) {
        putQuickCalendarEntry(thisFormData);
    } else {
        window.alert("Incomplete data!\nCheck input and try again!\n" + thisFormDataJ);
    }
}

function actOnHiddenToggle(event) {
    dojo.stopEvent(event);
    if(isSet(sessionVars.hiddenFeatures) && sessionVars.hiddenFeatures === "Enabled") {
        showNotice("Hidden features disabled!");
    } else {
        showNotice("Hidden features enabled!");
    }
    var thisFormData = dojo.formToObject(this.form);
    setSessionVariable("hiddenFeatures", thisFormData.Hidden);
    getAnthonyOverviewData();
}

function generteLinkSpinner(links) {
    var rData = "";
    var cubeRes = "width=128 height=128";
    var numElements = links.length;
    var elementListX = [];
    var i = 0;
    links.forEach(function (link) {
        var thisElement = "<a styleReplace href='";
        if(!checkMobile() && isSet(link.DesktopLink)) { thisElement += link.DesktopLink; } else { thisElement += link.URL; }
        thisElement += "' target='newWindow" + i + "'>" +
                "<img " + cubeRes + " src='" + getBasePath("pageSnaps") + "/" + link.Bubble + ".png' alt='" + link.Description + "'></a>";
        elementListX.push(thisElement);
    });
    rData += "<div style='min-height: 24px;'></div>" +
            imageLinks3d(elementListX, 25, 208, 2.25);
    dojo.byId("linkList").innerHTML = rData;
}

function changeH1Font() {
	var newFont = document.getElementById("newH1Font").value;
	document.getElementById("mainH1").style.fontFamily='" + newFont + "';
	//eval(newFontCSS);
}

function get3dLinkList() {   
    aniPreload("on");
    var thePostData = {
        "master": "Anthony.php-0"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("WebLinks"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    generteLinkSpinner(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for 3D Link List FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                }),
                $("#WxNews").toggle();;
    });
}

function getAnthonyOverviewData() {
    aniPreload("on");
    var thePostData = { "doWhat": "LogCharts" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    getAnthonyOverviewDataRawData();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Log Charts FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getAnthonyOverviewDataRawData() {
    aniPreload("on");
    var thePostData = "doWhat=AnthonyOverviewData";
    var xhArgs = {
        preventCache: true,
        url: getResource("Logs"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            showInLogs(
                data.dbInfo,
                data.webVersion,
                data.sduLogs,
                data.camLogs,
                data.backupLogs
            );
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for AnthonyOverviewData FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function showInLogs(dbInfo, webVersion, sduLogs, camLogs, backupLogs) {
    var rData;
    var mySqlOverallSize = 0;
    var mySqlOverallRows = 0;
    var toolHolder = "<h4>Tools</h4>";
    var quickWebCalEntryForm = "<div class='UBox' id='QuickCalendar'><span>Quick Calendar Entry</span>" +
            "<div class='table'><form class='tr' id='QuickCalFormTr'>" +
            "<span class='td'><input name='QuickStart' type='date' value='' style='width: " + dateEntryWidth + "px;'/></span>" +
            "<span class='td'><input name='QuickStartTime' type='time' value='' style='width: " + timeEntryWidth + "px'/></span>" +
            "<span class='td'><input name='QuickTitle' type='text' value='' style='width: 100px;' />" +
            "<input name='doWhat' type='hidden' value='setQuickCalEntry'/>" +
            "<button id='QuickCalBtn' name='QuickCalendar' class='UButton'>Go</button>" +
            "</span></form></div></div>";
    toolHolder += quickWebCalEntryForm;
    var bubbleHolder = "<h4>Bubbles</h4>";
    var databaseInfoBubble = "<div class='UPopNM'>" +
            "<a href='" + getBasePath("ui") + "/DBInfo.jsp' target='new'>" +
            "<button class='UButton'>Database" +
            "<div class='UPopNMO'>";
    dbInfo.forEach(function (dbi) {
        mySqlOverallSize += dbi.DBSize;
        mySqlOverallRows += dbi.DBRows;
    });
    databaseInfoBubble += "<strong>MySQL</strong><br/>" + autoUnits(mySqlOverallSize) + "b, " + autoUnits(mySqlOverallRows) + " rows.<p>" +
            "</div></button></a></div>";
    // Postgres DB Info build query?
    var wvbCols = [ "Version", "Date", "Changes", "Revisions" ];
    var webVersionBubble = "<div class='UPopC'><button class='UButton'>Versions" +
            "<div class='UPopCO'>" +
            "<table><thead><tr>";
    for(var i = 0; i < wvbCols.length; i++) { webVersionBubble += "<th>" + wvbCols[i] + "</th>"; }
    webVersionBubble += "</tr></thead><tbody>";
    webVersion.forEach(function (wv) {
    	webVersionBubble += "<tr>" +
    		"<td>" + wv.Version + "</td>" +
			"<td>" + wv.Date + "</td>" + 
			"<td>" + wv.Changes + "</td>" +
			"<td>" + wv.Revisions + "</td>" + 
			"</tr>";
    	}
    );
    webVersionBubble += "</tbody></table></div></button></div>";
    var sduCols = [ "Run #", "Size", "Notes", "Date/Time" ];
    var sduBubble = "<div class='UPopC'><button class='UButton'>SDUtils" +
            "<div class='UPopCO'>" +
            "<table><thead><tr>";
    for(var i = 0; i < sduCols.length; i++) { sduBubble += "<th>" + sduCols[i] + "</th>"; }
    sduBubble += "</tr></thead><tbody>";
    sduLogs.forEach(function (sdu) {
        sduBubble += "<tr>" +
                "<td>" + sdu.EventID + "</td>" +
                "<td>" + ((sdu.ZIPSize)/1024/1024).toFixed(2) + " GB</td>" +
                "<td>" + sdu.Notes + "</td>" +
                "<td>" + sdu.Date + " " + sdu.Time + "</td>" +
                "</tr>";
    });
    sduBubble += "</tbody></table></div></button></div>";
    var camCols = [ "Date", "MP4<br/>", "Frames" ];
    var camLogBubble = "<div class='UPopC'>" +
            "<a href='" + doCh("j", "LogCamsMp4", null) + "' target='new'>" +
            "<button class='UButton'>Cams" +
            "<div class='UPopCO'>" +
            "<table><thead><tr>";
    for (var i = 0; i < camCols.length; i++) { camLogBubble += "<th>" + camCols[i] + "</th>"; }
    camLogBubble += "</thead><tbody>";
    camLogs.forEach(function (clog) {
        var icFriendly = "N/A";
        if(isSet(clog.ImgCount)) { icFriendly = clog.ImgCount; }
        camLogBubble += "<tr>" +
                "<td>" + clog.Date + "</td>" + 
                "<td>" + ((clog.MP4Size)/1024).toFixed(1) + " MB</td>" +
                "<td>" + icFriendly + "</td>" +
                "</tr>";
    });
    camLogBubble += "</tbody></table></div></button></a></div>";
    var buCols = [ "Date", "Type<br/>Errors", "Min.<br/>Elapsed", "GB<br/>Used", "Diff" ];
    var backupLogBubble = "<div class='UPopC'><button class='UButton'>Backups" + 
            "<div class='UPopCO'>" +
            "<table><thead><tr>";
    for(var i = 0; i < buCols.length; i++) { backupLogBubble += "<th>" + buCols[i] + "</th>"; }
    backupLogBubble += "</thead><tbody>";
    backupLogs.forEach(function (blog) {
        backupLogBubble += "<tr>" +
                "<td>" + blog.Date + "</td>" +
                "<td><div class='UPop'>" + blog.Type +
                "<div class='UPopO'>" +
                "OS Version: " + blog.OSVersion + "<br/>" +
                "Errors: " + blog.Errors +
                "</div></div></td>" +
                "<td>" + ((blog.Time)/60).toFixed(1) + "</td>" +
                "<td>" + blog.GB_Used + "<br/>" + ((blog.GB_Used/blog.GB_Capacity)*100).toFixed(1) + " %</td>" +
                "<td>" + blog.rsyncDiff + "</td>" +
                "</tr>";
    });
    backupLogBubble += "</tbody></table></div></button></div>";
    bubbleHolder += databaseInfoBubble + webVersionBubble + sduBubble + camLogBubble + backupLogBubble;
    rData += toolHolder + bubbleHolder + "<br/>" +
            "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=PointClick' target='new'>Geo Point Click Map</a><p>";
    var newHiddenValue = "Enabled";
    if(isSet(hiddenFeatures) && hiddenFeatures === "Enabled") { newHiddenValue = "Disabled"; }
    if(sessionVars.userName === "f00dl3") {
        var hiddenFeaturesToggler = "<div class='UBox' id='HiddenStuff'>" +
                "<form id='HiddenForm'><span>Hidden Features</span><br/>" +
                "<input type='checkbox' id='HiddenCheckbox' name='Hidden' value='" + newHiddenValue + "'>" + newHiddenValue + "</input><br/>" +
                "<noscript><input id='HiddenButton' type='submit' name='DoShowHidden' /></noscript>" +
                "</form></div>";
        rData += hiddenFeaturesToggler + "<p>";
    }
    var javaScriptSchit = "<input type='text' id='newH1Font' placeholder='H1 Font'/>" +
            " <input type='button' id='setFontGo' value='Do it!' /></p>";

    var uploadPath = getResource("Upload");
    var fileUploadForm = "<br/><form method='POST' enctype='multipart/form-data' action='" + uploadPath + "'>" +
                "File to upload: <input type='file' name='upfile'><br/>" +
                "Notes about the file: <input type='text' name='note'><br/>" +
                "<br/>" +
                "<input type='submit' value='Press'> to upload!" +
                "</form>";
    rData += javaScriptSchit + fileUploadForm + "</div>";
    dojo.byId("inLogs").innerHTML = rData;
    var hiddenCheckbox = dojo.byId("HiddenCheckbox");
    var quickCalButton = dojo.byId("QuickCalBtn");
    var changeFontButton = dojo.byId("setFontGo");
    dojo.connect(hiddenCheckbox, "onchange", actOnHiddenToggle);
    dojo.connect(quickCalButton, "click", actOnCalendarSubmit);
    dojo.connect(changeFontButton, "click", changeH1Font);
    $("#inLogs").toggle();
}

function logButtonListener() {
    var logButton = dojo.byId("Sh_inLogs");
    dojo.connect(logButton, "onclick", getAnthonyOverviewData);
}

function popLinkList() {
    if(checkMobile()) {
        getWebLinks("Anthony.php-0", "linkList", "list");
    } else {
        get3dLinkList();
    }
}

function putQuickCalendarEntry(formData) {
    aniPreload("on");
    var xhArgs = {
        preventCache: true,
        url: getResource("WebCal"),
        postData: formData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            aniPreload("off");
            showNotice("Calendar entry " + data.EntryID + " added!");
            console.log(data.ErrorLog);
            getAnthonyOverviewData();
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("request for QuickCal Entry FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

var initAnthony = function(event) {
    getObsDataMerged("disHolder", "marquee");
    getWebLinks("Anthony.php-SSH", "sshLinks", "bubble");
    popLinkList();
    getWebVersion("versionPlaceholder");
    logButtonListener();
};

dojo.ready(initAnthony);