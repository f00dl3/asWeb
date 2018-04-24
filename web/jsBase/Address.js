/* 
by Anthony Stump
Created: 22 Apr 2018
Updated: 23 Apr 2018
 */

var addresses;

function getAddresses() {
    dojo.byId("ResultHolder").innerHTML = "Loading address book data...";
    aniPreload("on");
    var thePostData = { "doWhat": "getAddresses" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Addresses"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    addresses = data;
                    populateSearchHolder();
                    populateResults(addresses);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Address Book FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateHead() {
    var rData = "";
    if(isSet(hiddenFeatures)) { rData += "<a href='" + getBasePath("ui") + "/Congress.jsp' target='new'>Congress DNC Hack</a><br/>"; }
    rData += "<a href='" + getBasePath("old") + "/OutMap.php?AllPoints=Addresses' target='new'>Map them all!</a><p>";
    dojo.byId("HeadHolder").innerHTML = rData;
}

function populateResults(addressData) {
    var rData = "",
            address = "",
            birthday = "",
            category = "",
            displayName = "",
            eMail = "",
            pBusiness = "",
            pCell = "",
            pCell2 = "",
            pHome = "",
            point = "",
            website = "";
    var contactCount = (addressData.length) + " contacts returned.";
    addressData.forEach(function (ab) {
        if(isSet(ab.Business)) { displayName = ab.business; }
        if(isSet(ab.FirstName) && isSet(ab.LastName)) { displayName = ab.LastName + ", " + ab.FirstName; }
        if(isSet(ab.Category)) { category = "<strong>Category: </strong>" + ab.Category + "<br/>"; }
        if(isSet(ab.Birthday)) { birthday = "<strong>Birthday(s): </strong>" + ab.Birthday + "<br/>"; }
        if(isSet(ab.Website)) { website = "<strong>Website: </strong>" + ab.Website + "<br/>"; }
        if(isSet(ab.P_Business)) {
            pBusiness = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_off.jpeg'/>" +
                    "<div class='UPopO'><a href='tel:" + ab.P_Business + "' target='phone'>" + ab.P_Business + "</a></div></div>";
        }
        if(isSet(ab.P_Home)) {
            pHome = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_hom.gif'/>" +
                    "<div class='UPopO'><a href='tel:" + ab.P_Home + "' target='phone'>" + ab.P_Home + "</a></div></div>";
        }
        if(isSet(ab.P_Cell)) {
            pCell = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_mob.png'/>" + 
                    "<div class='UPopO'><a href='tel:" + ab.P_Cell + "' target='phone'>" + ab.P_Cell + "</a></div></div>";
        }
        if(isSet(ab.P_Cell2)) {
            pCell2 = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_mob.png'/>" +
                    "<div class='UPopO'><a href='tel:" + ab.P_Cell2 + "' target='phone'>" + ab.P_Cell2 + "</a></div></div>";
        }
        if(isSet(ab.EMail)) {
            eMail = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_ema.png'/>" +
                    "<div class='UPopO'><a href='mailto:" + ab.EMail + "' target='new'>" + ab.EMail + "</a></div></div>";
        }
        if(isSet(ab.Point)) {
            point = "[<a href='" + getBasePath("old") + "/OutMap.php?Title=" + displayName + "&Point=" + ab.Point + "' target='new'>Local</a>] "
        }
        if(isSet(ab.Zip)) {
            address = "<div class='UPopNM'><img class='th_icon' src='" + getBasePath("icon") + "/ic_map.jpeg'/>" +
                    "<div class='UPopNMO'>" + ab.Address + "<br/>" + ab.City + ", " + ab.State + " " + ab.Zip + "<br/>" +
                    "<a href='https://www.google.com/maps/search/" + ab.Address + "," + ab.City + " " + ab.State + " " + ab.Zip + "' target='new'>Google</a> " +
                    point + "</div></div>";
        }
        rData += "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + displayName +
                "<div class='UPopO'>" + website + category + birthday + "<strong>As of: </strong>" + ab.AsOf + "</div></div></span>" +
                "<span class='td'>" + pBusiness + pHome + pCell + pCell2 + eMail + address + "</span>" +
                "</div>";
    });
    dojo.byId("ResultHolder").innerHTML = rData;
    dojo.byId("CountHolder").innerHTML = contactCount;
}

function populateSearchHolder() {
    var rData = "<div class='table'>" +
            "<form class='tr' id='AddressSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBox' name='AddressSearchField' onKeyUp='showAddressHint(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div><p>";
    dojo.byId("SearchHolder").innerHTML = rData;
}

function showAddressHint(value) {
        if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        addresses.forEach(function (sr) {
            if(
                (isSet(sr.Business) && (sr.Business).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.LastName) && (sr.LastName).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.FirstName) && (sr.FirstName).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Address) && (sr.Address).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.City) && (sr.City).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.State) && (sr.State).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        populateResults(matchingRows, hitCount, matchLimitHit);    
    }
}

function init() {
    populateHead();
    getAddresses();
}

dojo.ready(init);