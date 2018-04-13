/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 12 Apr 2018
 */

var annMaint = 910.66;
var annMiles = 12672;
var bicycleUsed = "A16";
var carOwnershipYears = (2018-2010);
var carStartMiles = 44150;
var costPerMile = 3.50;
var cpmNoMpg = (annMaint / annMiles);
var elecCost = 0.14;
var timeOutMilli = (60*1000);
var hiddenFeatures = 0;
var playIcon = "<img class='th_icon' src='" + getBasePath("icon") + "/ic_ply.png' />";

$(window).on('load', function() {
    aniPreload("off");
});

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
    return tColor;
}

function autoFontScale(tPercent) {
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
    return tInput.replace('/[^A-Za-z0-9._]/', ' ');
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
    var dBack = "";
    switch(type) {
        case "p": 
            dBack += getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=" + dynVar;
            if(isSet(opts)) { dBack += "&" + opts; }
            break;
        case "j":
            window.alert("Use XHR?");
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
        case "dateOnly": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyy-MM-dd", selector: "date"}); break;
        case "full": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyy-MM-dd HH:mm:ss", selector: "date" }); break;
        case "js": fmtDate = inDate; break;
        case "timestamp": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyyMMddHHmmSS", selector: "date" }); break;
        case "yearMonth": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyyMM", selector: "date"}); break;
        case "yyyy-MM": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyy-MM", selector: "date"}); break;
        case "yearOnly": fmtDate = dojo.date.locale.format(inDate, {datePattern: "yyyy", selector: "date"}); break;
    }
    return fmtDate;
}

function getBasePath(opt) {
    var base = self.location.protocol + "//" + self.location.host;
    var baseForUi = "/asWeb";
    var shortBaseForRestlet = baseForUi + "/r";
    var fullBaseForRestlet = base + shortBaseForRestlet;
    var baseForRestlet = fullBaseForRestlet;
    var tBase = "";
    tBase = base.split(":")[1];
    if(checkMobile()) { tBase += ":8082"; }
    tBase = "https:" + tBase;
    switch(opt) {
        case "chartCache": tBase = base + "/chartCache"; break;
        case "congress": tBase = baseForUi + "/img/CongressHack"; break;
        case "g2OutOld": tBase += "/G2Out"; break;
        case "getOld": tBase += "/Get"; break;
        case "icon": tBase = baseForUi + "/img/Icons"; break;
        case "image": tBase = baseForUi + "/img"; break;
        case "media": tBase += "/MediaServ"; break;
        case "rest": tBase = baseForRestlet; break;
        case "old": tBase += "/ASWebUI"; break;
        case "osmTiles": tBase += "/osm_tiles/"; break;
        case "tomcatOld": tBase += "/Tomcat"; break;
        case "ui": tBase = baseForUi; break;
    }
    return tBase;
}

function getDate(inType, inInput, rdFormat) {
    dojo.require("dojo.date");
    var initDate = new Date();
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

function getRelatedLinks(page) {
    var args = {
        url: getResource("WebLinks"),
        handleAs: "json",
        postData: "master=" + page,
        timeout: timeOutMilli,
        load: function(data) {
            return data;
        },
        error: function(data, iostatus) {
            window.alert("xhrGet RelatedLinks for "+page+": FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(args);
}

function getResource(what) {
    switch(what) {
        case "Chart": return getBasePath("rest") + "/Chart";
        case "Congress": return getBasePath("rest") + "/Congress";
        case "Cooking": return getBasePath("rest") + "/Cooking";
        case "Entertainment": return getBasePath("rest") + "/Entertainment";
        case "Fitness": return getBasePath("rest") + "/Fitness";
        case "Finance": return getBasePath("rest") + "/Finance";
        case "Home": return getBasePath("rest") + "/Home";
        case "Logs": return getBasePath("rest") + "/Logs";
        case "Pto": return getBasePath("rest") + "/PTO";
        case "WebLinks": return getBasePath("rest") + "/WebLinks";
        case "Wx": return getBasePath("rest") + "/Wx";
    }
}

function getSum(numbers) {
    return numbers.reduce(function (a, b) {
        return a + b;
    });
}

function getWebLinks(master, whereTo, a3dFlags) {
    var arXhr1 = {
        url: getResource("WebLinks"),
        handleAs: "json",
        postData: "master=" + master,
        timeout: timeOutMilli,
        load: function(data) {
            if(isSet(whereTo)) {
                putWebLinks(data, whereTo, a3dFlags);
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
            var thisDiv = "<div class='UPop'>v" + theData.Version + " (Updated: " + theData.Date + ")";
            thisDiv += "<div class='UPopO'>" + theData.Changes + "</div></div>";
            dojo.byId(whereTo).innerHTML = thisDiv;
        },
        error: function(data, iostatus) {
            window.alert("xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrWebVersionArgs);
}

function imageLinks3d(elems, maxW, maxH, tFact) {
	var numElems = elems.length;
        var rotation = 0;
	var radius = Math.floor(320/(2*Math.tan(deg2rad(180/(numElems/tFact)))));
	var genOut = "<div id='stage' style='" +
                " width: " + maxW + "px; height: " + maxH + "px;";
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

function nodeState(tNode, state) {
    switch(state) {
        case "online": return "<button style='background-color: #666666; color: white;'>" + tNode + "</button>"; break;
        case "offline": return "<button style='background-color: yellow; color: black;'>" + tNode + "</button>"; break; 
    }
}

function putNavi() {
    var uiBasePage = getBasePath("ui") + "/Anthony.jsp";
    var goHome = "<a href='" + getBasePath("ui") + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_hom.gif'/></a>" +
            "<a href='" + getBasePath("old") + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_gar.png' /></a>";
    rData = "<div class='Navi'>" + goHome + "<div class='NaviO'>" +
            "<span>" + goHome + " (Logout)</span>" +
            "<span id='naviLinks'></span>";
    dojo.byId("NaviHolder").innerHTML = rData;
    getWebLinks("Anthony.php-0", "naviLinks", null);
}

function putWebLinks(data, whereTo, a3dFlags) {
    var placeholder;
    var numElems = data.length;
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
        placeholder += "<li><a href='"+theLink+"'>";
        placeholder += theData.Description;
        placeholder += "</a></li>";
        placeholder += "</ul>";
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

var init = function(event) {
    putNavi();
};

dojo.ready(init);