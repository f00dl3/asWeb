/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 21 Mar 2018
 */

var annMaint = 910.66;
var annMiles = 12672;
var carOwnershipYears = (2018-2010);
var carStartMiles = 44150;
var cpmNoMpg = (annMaint / annMiles);
var timeOutMilli = (60*1000);
var hiddenFeatures = 0;
var costPerMile = 3.50;

function animatedArrow(thisArrow) {
    switch(true) {
        case (thisArrow > 0): return "<img class='arrow' src='"+getBasePath("ui")+"/img/Icons/ar_up.gif'/>";
        case (thisArrow < 0): return "<img class='arrow' src='"+getBasePath("ui")+"/img/Icons/ar_dn.gif'/>";
        case (thisArrow === 0): return "";
    }
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
    if (tVal > 1000) { tVal = round(tVal/1000,1); tSuffix = "K"; }
    if (tVal > 1000) { tVal = round(tVal/1000,1); tSuffix = "M"; }
    if (tVal > 1000) { tVal = round(tVal/1000,1); tSuffix = "G"; } 
    if (tVal > 1000) { tVal = round(tVal/1000,1); tSuffix = "T"; } 
    if (tVal > 1000) { tVal = round(tVal/1000,1); tSuffix = "P"; } 
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
    var tBase = " ";
    tBase = base.split(":")[1];
    if(checkMobile()) { tBase += ":8082"; }
    tBase = "https:" + tBase;
    switch(opt) {
        case "icon": tBase = baseForUi + "/img/Icons"; break;
        case "media": tBase += "/MediaServ"; break;
        case "rest": tBase = baseForRestlet; break;
        case "old": tBase += "/ASWebUI"; break;
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

function getResource(what) {
    switch(what) {
        case "Fitness": return getBasePath("rest") + "/Fitness";
        case "Finance": return getBasePath("rest") + "/Finance";
    }
}

function iLinks3d(elems, maxW, maxH, tFact) {
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
            genOut += element.replace("/styleReplace/", thisCSS);
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

function searchAhead(dataArray, thisQuery, matchLimit) {
    var itemMatchLimitHit, matchedItems, ti, dataBack, noticeBack, objectBack;
    itemMatchLimitHit = matchedItems = ti = 0;
    objectBack = {};
    var thisHint = "";
    if(thisQuery !== "") {
        var thisQueryLength = thisQuery.length;
        thisQuery.forEach(function (item) {
           if(dataArray.indexOf(thisQuery) >= 0) {
               matchedItems++;
               if(thisHint === "") {
                   thisHint = dataArray[ti];
               } else if (matchedItems > matchLimit) {
                   itemMatchLimitHit = 1;
                   thisHint = "";
               } else {
                   thisHint += dataArray[ti];
               }
           }
           ti++;
        });
    }
    if(isSet(thisHint)) {
        if(isSet(itemMatchLimitHit)) {
            noticeBack = "<div class='Notice' style='background-color: red; color: white;'>Showing " + matchLimit + " of " + matchedItems + " results!</div>";
        } else {
            noticeBack = "<div class='Notice'>" + matchedItems + " results found!</div>";
            dataBack = thisHint ;
        }
    } else {
        noticeBack = "</div><div class='Notice'>Unable to find anythin!</div>";
    }
    objectBack.noticeBack = noticeBack;
    objectBack.dataBack = dataBack;
    return objectBack;
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
                            
function ReturnWebLinks($thisMaster) {
	global $dbpdo;
	global $query_WebLinks;
	$thisMaster = "%" . $thisMaster . "%";
	$stmt = $dbpdo -> prepare($query_WebLinks);
	$stmt -> bindValue(":Master", $thisMaster, PDO::PARAM_STR);
	$stmt -> execute();
	while ($row = $stmt -> fetch(PDO::FETCH_ASSOC)) { $rows[] = $row; }
	return $rows;
}
                            
class SortedIterator extends SplHeap {
public function __construct(Iterator $iterator) { foreach($iterator as $item) { $this->insert($item); } }
public function compare($b, $a) { return strcmp($a->getRealpath(), $b->getRealpath()); }
}
                            
 */