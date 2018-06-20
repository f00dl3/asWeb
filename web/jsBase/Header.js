/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 20 Jun 2018
 */

var annMaint = 910.66;
var annMiles = 12672;
var bicycleUsed = "A16";
var carOwnershipYears = (2018-2010);
var carStartMiles = 44150;
var costPerMile = 3.50;
var cpmNoMpg = (annMaint / annMiles);
var elecCost = 0.14;
var hiddenFeatures = 0;
var timeOutMilli = (60*1000);
var playIcon = "<img class='th_icon' src='" + getBasePath("icon") + "/ic_ply.png' />";

var timeEntryWidth = 110;
var dateEntryWidth = 75;
if(checkMobile()) {
    timeEntryWidth = 75;
    dateEntryWidth = 60;
}

if(isSet(window.localStorage.getItem("sessionVars"))) {
    var sessionVars = JSON.parse(window.localStorage.getItem("sessionVars"));
    hiddenFeatures = sessionVars.hiddenFeatures;
}

$(window).on('load', function() {
    aniPreload("off");
});

function actOnLogout(event) {
    dojo.stopEvent(event);
    setSessionVariable("loggedIn", "false");
    window.alert("Attempted logout!");
    window.location.href = getResource("Landing");
}

function animatedArrow(thisArrow) {
    switch(true) {
        case (thisArrow > 0): return "<img class='arrow' src='"+getBasePath("ui")+"/img/Icons/ar_up.gif'/>";
        case (thisArrow < 0): return "<img class='arrow' src='"+getBasePath("ui")+"/img/Icons/ar_dn.gif'/>";
        case (thisArrow === 0): return "";
    }
}

function aniPreload(turn) {
    if(!checkMobile()) { aniPreloadGetSize(); }
    switch(turn) {
        case "on": $(".preload").fadeIn("slow", function() { $(this).show(); }); break;
        case "off": $(".preload").fadeOut("slow", function() { $(this).hide(); }); break;
    }
}

function aniPreloadGetSize() {
    setInterval(function() { 
        var pageLoadSizeKB = ($("html").html().length/1024);
        dojo.byId("preloadSize").innerHTML = Math.round(pageLoadSizeKB) + " KB";
    }, 250);
}

function autoColorScale(tData,tMax,tMin,tForcedAvg) { 
    var tAverage, tJump, tColor;
    var tAverage = tJump = 0;
    var tColor = "";
    if(isSet(tForcedAvg)) { tAverage = tForcedAvg; } else { tAverage = (tMax + tMin)/2; }
    tJump = (tMax - tMin)/12;
    switch(true) {
        case (tData >= tAverage+(5*tJump) && tData < tAverage+(6*tJump)): tColor = "#ff00cc"; break;
        case (tData >= tAverage+(4*tJump) && tData < tAverage+(5*tJump)): tColor = "#660000"; break;
        case (tData >= tAverage+(3*tJump) && tData < tAverage+(4*tJump)): tColor = "#ff0000"; break;
        case (tData >= tAverage+(2*tJump) && tData < tAverage+(3*tJump)): tColor = "#ff6600"; break;
        case (tData >= tAverage+(1*tJump) && tData < tAverage+(2*tJump)): tColor = "#ff9900"; break;
        case (tData >= tAverage && tData < tAverage+(1*tJump)): tColor = "#ffff00"; break;
        case (tData < tAverage && tData >= tAverage-(1*tJump)): tColor = "#00ff00"; break;
        case (tData <= tAverage-(1*tJump) && tData > tAverage-(2*tJump)): tColor = "#33ffff"; break;
        case (tData <= tAverage-(2*tJump) && tData > tAverage-(3*tJump)): tColor = "#3399ff"; break;
        case (tData <= tAverage-(3*tJump) && tData > tAverage-(4*tJump)): tColor = "#0000ff"; break;
        case (tData <= tAverage-(4*tJump) && tData > tAverage-(5*tJump)): tColor = "#000099"; break;
        case (tData <= tAverage-(5*tJump) && tData > tAverage-(6*tJump)): tColor = "#660099"; break;
        default: tColor = "#ffffff"; break;
    }
    //console.log("AutoColor: " + tColor);
    return tColor;
}

function autoFontScale(tPercent) {
    var fntColor = "gray";
    switch(true) {
        case (tPercent <= 28): fntColor = "white"; break;
        default: fntColor = "black"; break;
    }
    return fntColor;
}

function autoUnits(tVal) {
    var tSuffix = "";
    if (tVal < 0) { tVal = ""; tSuffix = "Error!"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "K"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "M"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "G"; } 
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "T"; } 
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "P"; } 
    var formatting = tVal + tSuffix;
    return formatting;
}

function basicInputFilter(tInput) {
    return tInput.replace(/[^a-zA-Z0-9]/g,'');
}

function checkMobile() {
    if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
        return true;
    } else {
        return false;
    }
}

function deg2rad(degrees) {
    return degrees * Math.PI/180;
}

function doCh(type, dynVar, opts) {
    var timestamp = getDate("day", 0, "timestamp");
    var dBack = "";
    var chFileName = dynVar + ".png";
    switch(type) {
        case "p": 
            dBack += getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=" + dynVar;
            if(isSet(opts)) { dBack += "&" + opts; }
            break;
        case "j":
            if(opts === "t") { chFileName = "th_" + chFileName; }
            dBack += getBasePath("chartCache") + "/" + chFileName + "?" + timestamp;
            break;
    }
    return dBack;
}

function doRefresh(period) {
    var dateString = getDate("day", 0);
    var returnString = "<meta http-equiv='refresh' content='" + period + "'>";
    returnString += "<div class='Notice'>Refreshed " + dateString + "</div>";
    return returnString;
}

function formatDate(inDate, request) {
    var fmtDate;
    dojo.require("dojo.date.locale");
    switch(request) {
        case "dateOnly": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyy-MM-dd", selector: "date"}); break;
        case "full": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyy-MM-dd HH:mm:ss", selector: "date" }); break;
        case "hourstamp": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyyMMddHH", selector: "date" }); break;
        case "js": fmtDate = inDate; break;
        case "timestamp": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyyMMddHHmmSS", selector: "date" }); break;
        case "yearMonth": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyyMM", selector: "date"}); break;
        case "yearOnly": fmtDate = dojo.date.locale.format(inDate, { datePattern: "yyyy", selector: "date"}); break;
        default: fmtDate = dojo.date.locale.format(inDate, { datePattern: request, selector: "date"}); break;
    }
    return fmtDate;
}

function getBasePath(opt) {
    var base = self.location.protocol + "//" + self.location.host;
    var baseForUi = "/asWeb";
    var shortBaseForRestlet = baseForUi + "/r";
    var baseForRestlet = base + baseForUi + "/r";
    var baseForServlet = base + baseForUi + "/s";
    var tBase = "";
    tBase = base.split(":")[1];
    if(checkMobile()) { tBase += ":8082"; }
    tBase = "https:" + tBase;
    switch(opt) {
        case "chartCache": tBase = baseForUi + "/cache"; break;
        case "congress": tBase = baseForUi + "/img/CongressHack"; break;
        case "downloads": tBase = baseForUi + "/Download"; break;
        case "g2OutOld": tBase += "/G2Out"; break;
        case "oldGet": case "getOld": case "getOldGet": tBase += "/Get"; break;
        case "icon": tBase = baseForUi + "/img/Icons"; break;
        case "image": tBase = baseForUi + "/img"; break;
        case "media": tBase += "/MediaServ"; break;
        case "rest": tBase = baseForRestlet; break;
        case "serv": tBase = baseForServlet; break;
        case "old": tBase += "/ASWebUI"; break;
        case "oldRoot": tBase = tBase; break;
        case "osmTiles": tBase += "/osm_tiles/"; break;
        case "pageSnaps": tBase = baseForUi + "/img/PageSnaps"; break;
        case "tomcatOld": tBase = baseForUi + "/x"; break;
        case "ui": tBase = baseForUi; break;
    }
    return tBase;
}

function getDate(inType, inInput, rdFormat, initialDate) {
    dojo.require("dojo.date");
    var initDate = new Date();
    if(isSet(initialDate)) {
        initDate = formatDate(initialDate, rdFormat);
    }
    var retDate = formatDate(dojo.date.add(initDate, inType, inInput), rdFormat);
    return retDate;
}

function getDivLoadingMessage(target) {
    dojo.byId(target).innerHTML = "Loading content...";
}

function getGetParams() {
    var $_GET = {};
    if(document.location.toString().indexOf('?') !== -1) {
        var query = document.location
                .toString()
                .replace(/^.*?\?/, "")
                .replace(/#.*$/, "")
                .split("&");
        for(var i = 0; i < query.length; i++) {
            var aux = decodeURIComponent(query[i].split("="));
            $_GET[aux[0]] = aux[1];
        }
    }
    return $_GET;
}

function getRefresh(measure) {
    switch(measure) {
        case "rapid": return 1 * 1000;
        case "short": return 30 * 1000;
        case "medium": return 90 * 1000;
        case "long": return 300 * 1000;
        case "veryLong": return 500 * 1000;
        default: return 90 * 1000;
    }
}

function getRelatedLinks(page) {
    aniPreload("on");
    var thePostData = { "master": page };
    require(["dojo/request"], function(request) {
        request
            .get(getResource("WebLinks"), {
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    return(data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for RelatedLinks / " + page + " FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getResource(what) {
    switch(what) {
        case "Addresses": return getBasePath("rest") + "/Addresses"; break;
        case "Anthony": case "f00dl3": return getBasePath("ui") + "/Anthony.jsp"; break;
        case "Cams": return getBasePath("ui") + "/Cams.jsp"; break;
        case "Chart": return getBasePath("rest") + "/Chart"; break;
        case "Congress": return getBasePath("rest") + "/Congress"; break;
        case "Cooking": return getBasePath("rest") + "/Cooking"; break;
        case "DBInfo": return getBasePath("rest") + "/DBInfo"; break;
        case "Emily": return getBasePath("ui") + "/Emily.jsp"; break;
        case "Entertainment": return getBasePath("rest") + "/Entertainment"; break;
        case "Fitness": return getBasePath("rest") + "/Fitness"; break;
        case "Finance": return getBasePath("rest") + "/Finance"; break;
        case "Home": return getBasePath("rest") + "/Home"; break;
        case "Landing": return getBasePath("ui"); break;
        case "Login": return getBasePath("rest") + "/Login"; break;
        case "Logs": return getBasePath("rest") + "/Logs"; break;
        case "MediaServer": return getBasePath("rest") + "/MediaServer"; break;
        case "NewsFeed": return getBasePath("rest") + "/NewsFeed"; break;
        case "Pto": return getBasePath("rest") + "/PTO"; break;
        case "Session": return getBasePath("serv") + "/Session"; break;
        case "SNMP": return getBasePath("rest") + "/SNMP"; break;
        case "Test": return getBasePath("rest") + "/Test"; break;
        case "TP": return getBasePath("rest") + "/TP"; break;
        case "WebCal": return getBasePath("rest") + "/WebCal"; break;
        case "WebLinks": return getBasePath("rest") + "/WebLinks"; break;
        case "WebVersion": return getBasePath("rest") + "/WebVersion"; break;
        case "Wx": return getBasePath("rest") + "/Wx"; break;
    }
}

function getServerPath(what) {
    switch(what) {
        case "apache2": return "/var/www"; break;
        case "tomcat": return "/var/lib/tomcat8/webapps"; break;
    }
}
function getSum(numbers) {
    return numbers.reduce(function (a, b) {
        return a + b;
    });
}

function getWebLinks(master, whereTo, outputType) {
    var arXhr1 = {
        url: getResource("WebLinks"),
        handleAs: "json",
        postData: "master=" + master,
        timeout: timeOutMilli,
        load: function(data) {
            if(isSet(whereTo)) {
                putWebLinks(data, whereTo, outputType);
            } else {
                return data;
            }
        },
        error: function(data, iostatus) {
            window.alert("xhrGet WebLinks: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(arXhr1);
}

function getWebVersion(whereTo) {
    var firstXhrUrl = getBasePath("rest")+"/WebVersion";
    var xhrWebVersionArgs = {
        url: firstXhrUrl,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = data[0];
            var thisDiv = "<div class='UPop'>v" + theData.Version + " (" + theData.Date + ")";
            thisDiv += "<div class='UPopO'>" + theData.Changes + "</div></div>";
            dojo.byId(whereTo).innerHTML = thisDiv;
        },
        error: function(data, iostatus) {
            window.alert("xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrWebVersionArgs);
}

function hideFooter() {
    $("#MAIN_FOOTER").hide();
}

function imageLinks3d(elems, maxW, maxH, tFact) {
	var numElems = elems.length;
        var rotation = 0;
	var radius = Math.floor(320/(2*Math.tan(deg2rad(180/(numElems/tFact)))));
	var genOut = "<div id='stage' style='" +
                " width: " + maxW + "px; height: " + maxH + "px;" +
                "'><div id='spinner'>";
	elems.forEach(function(element) {
            var thisCSS = "style='";
            thisCSS += "transform: rotateY(" + Math.round(rotation, 0) + "deg) translateZ(" + radius + "px);";
            thisCSS += " padding: 4px 4px;";
            thisCSS += "'";
            genOut += element.replace("styleReplace", thisCSS);
            rotation += (360 / numElems);
	});
	genOut += "</div></div>";
	return genOut;
}     

function inRange(value, low, high) {
        if (low > high) { var nHigh = low; var nLow = high; high = nHigh; low = nLow; }
	if (value > low && value <= high) { return value; }
	else { return !value; }
}

function isSet(varIn) {
    if(typeof varIn !== 'undefined' && varIn) {
        return true;
    } else { return false; }
}

function isSetAllowZero(varIn) {
    if(typeof varIn !== 'undefined' && varIn && varIn === 0) {
        return true;
    } else { return false; }
}

function isSetNotZero(varIn) {
    if(typeof varIn !== 'undefined' && varIn) {
        return true;
    } else { return false; }
}

function leafMapImageLink(relativePath, iWidth, iHeight) {
    return getBasePath("old") + "/OutMap.php?Image=Gallery&IW=" + iWidth + "&IH=" + iHeight + "&PicPath=" + relativePath;
}

function nodeState(tNode, state) {
    switch(state) {
        case "online": return "<button style='background-color: #666666; color: white;'>" + tNode + "</button>"; break;
        case "offline": return "<button style='background-color: yellow; color: black;'>" + tNode + "</button>"; break; 
    }
}

function olMapImageLink(olPicPath, olResolution, oldFlag) {
    var returnLink = getBasePath("ui") + "/OLMap.jsp?Action=Image&Input=" + olPicPath + "&Resolution=" + olResolution;
    if(isSet(oldFlag) && oldFlag) { returnLink += "&LegacyPath=Yes"; }
    return returnLink;
}

function putNavi() {
    var uiBasePage = getBasePath("ui");
    var overwriteUsername = "";
    if(isSet(sessionVars.userName)) {
        uiBasePage = getResource(sessionVars.userName);
        if(sessionVars.userName === "f00dl3") {
            overwriteUsername = "Anthony";
        } else {
            overwriteUsername = sessionVars.userName;
        }
        var goHome = "<a href='" + uiBasePage + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_hom.gif'/></a>" +
                "<a href='" + getBasePath("old") + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_gar.png' /></a>";
        var rData = "<div class='Navi'>" + goHome + "<div class='NaviO'>" +
                "<span>" + goHome + "</span>" +
                "<button class='SButton' id='LogoutSpan'>Logout</button><br/>" +
                "<span id='naviSshLinks'></span><br/>" +
                "<span id='naviLinks'></span>";
        dojo.byId("NaviHolder").innerHTML = rData;
        getWebLinks(overwriteUsername + ".php-0", "naviLinks", "list");
        if(overwriteUsername === "Anthony") {
            getWebLinks(overwriteUsername + ".php-SSH", "naviSshLinks", "bubble");
        }
    } else {
        dojo.byId("NaviHolder").innerHTML = "(Not logged in!)";
    }
    var logoutSpan = dojo.byId("LogoutSpan");
    dojo.connect(logoutSpan, "click", actOnLogout);
}

function putWebLinks(data, whereTo, outputType) {
    var placeholder = "";
    //var numElems = data.length;
    data.forEach(function (theData) {
        var theLink;
        if(checkMobile() && isSet(theData.DesktopLink)) {
            theLink = theData.DesktopLink;
        } else {
            if(isSet(theData.TomcatURL)) {
                theLink = theData.TomcatURL;
            } else {
                theLink = theData.URL;
            }
        }
        // Temporary link patches until Local Map Server moves to REST API
        if(theData.Description === "Local Map Server") { theLink = getBasePath("old") + "/Maps"; }
        switch(outputType) {
            case "bubble":
                    placeholder += "<a href='" + theLink + "' target='new'><button class='UButton'>" + theData.Bubble + "</button></a>";
                    break;
            case "list": default: 
                placeholder += "<li><a href='" + theLink + "'>" + theData.Description + "</a></li>";
                break;
        }
    });
    dojo.byId(whereTo).innerHTML = placeholder;
}

function timeMinutes(inMin) {
    return 1000*60*inMin;
}

function scLd(scriptName) {
    if (!scriptName) scriptName = getBasePath("ui")+"/js/"+scriptName+".js";
    var scripts = document.getElementsByTagName('script');
    for (var i = scripts.length; i--;) {
        if (scripts[i].src === scriptName) return true;
    }
    return false;
}

function showNotice(message) {
    dojo.byId("NoticeHolder").innerHTML = "<div class='Notice'>" + message + "</notice>";
    $('.Notice').fadeIn('slow').delay(5000).fadeOut('slow');
}

/* 

function AuthCheck($thisPage,$thisUser) {
	$thisUser = "%" . $thisUser . "%";
	$thisPage = strtok($thisPage, "?");
	global $dbpdo;
	global $query_WebPerms;
	$stmt = $dbpdo -> prepare($query_WebPerms);
	$stmt -> bindParam(":PageURI", $thisPage, PDO::PARAM_STR);
	$stmt -> bindParam(":UserCheck", $thisUser, PDO::PARAM_STR);
	$stmt -> execute();
	return $stmt -> rowCount();
}
                            
function getActivityType(inFile) {
    var activ;
    switch(substr(inFile, -1)) {
            case "C": case "D": activ = 'Cycling'; break;
            case "R": case "S": activ = 'Running'; break;
    }
    return activ;
} 
                            
function ExportCSV($input, $headers, $outFile) {
	ob_end_clean();
	$TempSpace = fopen('php://memory', 'w');
	fputcsv ($TempSpace, $headers, ',');
	foreach ($input as $line) {
		fputcsv($TempSpace, $line, ',');
	}
	fseek($TempSpace, 0);
	header('Content-Type: application/octet-stream');
	header('Content-Type: application/csv');
	header('Content-Disposition: attachment; filename="' . $outFile . date('Y-m-d_Hms') . '.csv";');
	fpassthru($TempSpace);
	exit();
}

function GetDirectorySize($path) {
	$bytestotal = 0;
	$path = realpath($path);
	if($path!==false) {
		foreach(new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path, FilesystemIterator::SKIP_DOTS)) as $object) {
			$bytestotal += $object->getSize();
		}
	} return $bytestotal;
}
                            
class SortedIterator extends SplHeap {
    public function __construct(Iterator $iterator) { foreach($iterator as $item) { $this->insert($item); } }
    public function compare($b, $a) { return strcmp($a->getRealpath(), $b->getRealpath()); }
}
                            
 */

function init() {
    getSessionVariables();
    putNavi();
}

dojo.ready(init);