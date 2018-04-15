/* 
 * by Anthony Stump
 * Created: 15 Apr 2018
 */

var legoData;

function displayLego() {
    var target = "ETLego";
    dojo.byId(target).innerHTML = "Loading...";
    putBrixSearchBox(target);
    getLego("LegoPopup");
    $("#"+target).toggle();
}

function getLego(legoHolder) {
    dojo.byId(legoHolder).innerHTML = "Loading JSON Lego data (~3 MB)...";
    aniPreload("on");
    var thePostData = { "doWhat": "getLego" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    legoData = data;
                    dojo.byId(legoHolder).innerHTML = "Lego JSON data ready!";
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Lego FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function legoHint(value) {
    if(value.length > 2) {
        console.log(value);
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        legoData.forEach(function (sr) {
            if(
                (isSet(sr.Number) && (sr.Number).toLowerCase().includes(value.toLowerCase())) ||
                //Errors out - still to debug!
                (isSet(sr.Year) && (sr.Year).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Variant) && (sr.Variant).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Theme) && (sr.Theme).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Subtheme) && (sr.Subtheme).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        returnBrixResults(matchingRows, hitCount, matchLimitHit);    
    }
}

function putBrixSearchBox(target) {
    var rData = "<h3>Lego Database</h3>" +
            "<div class='table'>" +
            "<form class='tr' id='LegoSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBrix' name='StationSearchField' onkeyup='legoHint(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form>" +
            "</div><p>" +
            "<div class='table' id='LegoPopup'></div>";
    dojo.byId(target).innerHTML = rData;
}

function returnBrixResults(legoData, hitCount, matchLimitHit) {
    var rData = "";
    var thumbDo = "";
    legoData.forEach(function (lego) {
        if(lego.ImgDown === 1) {
            thumbDo = "<a href='" + getBasePath("tomcatOld") + "/Lego/Full/" + lego.SetID + ".jpg' target='new'>" +
                    "<img class='th_icon' src='" + getBasePath("tomcatOld") + "/Lego/Thumb/" + lego.SetID + ".jpg'></a>";
        }
        rData += "<div class='tr'>" +
                "<span class='td'>" + thumbDo + lego.Number + "</span>" +
                "<span class='td'><div class='UPop'>" + lego.Name + "<div class='UPopO'>" +
                "<strong>Released:</strong> " + lego.Year + "<br/>" +
                "<strong>Set Cost:</strong> $" + lego.USPrice + "<br/>" +
                "<strong>Variant:</strong> " + lego.Variant + "<br/>" +
                "<strong>Theme:</strong> " + lego.Theme + "<br/>" +
                "<strong>Subtheme:</strong> " + lego.Subtheme + "<br/>" +
                "</div></div>" +
                "</span>" +
                "<span class='td'>" + lego.Pieces + "</span>" +
                "</div>";
    });
}