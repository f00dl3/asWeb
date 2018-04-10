/* 
by Anthony Stump
Created: 9 Apr 2018
 */

function getMyLocData(locMerged) {
    var whoLoggedIn = "f00dl3"; // get Logged IN User to var
    var myLocation;
    switch(whoLoggedIn) {
        case "f00dl3": myLocation = locMerged.f00dl3; break;
        case "Emily": myLocation = locMerged.Emily; break;
        default: myLocation = getHomeGeo("geoJSONr"); break;
    }
    var mapViewSetter = map.setView(new L.LatLng(myLocation), 8);
    return mapViewSetter;
}

function mapMobiLocA(aLoc) {
    if(isSet(aLoc.Location) && aLoc.Location !== "[,]") {
        var pTemp = conv2Tf(aLoc.BattTemp/10);
        var mCoordsJson = JSON.parse(aLoc.MobiLoc);
        var mobiLocation = mCoordsJson[1] + "," + mCoordsJson[0];
        if(mobiLocation === ',' || mobiLocation == 'null,null') {
            mobiLocation = getHomeGeo("geoJSONr");
        }
        // set session mobile location to mobiLocation
        var mobiContent = "Anthony (Note 3)<br/>" +
                "Ping: " + (aLoc.WalkTime).substring(6, 10) + "<br/>" +
                "Battery: " + aLoc.BattLevel + "%<br/>" +
                "Temp: <span style='" + styleTemp(pTemp) + "'>" + pTemp + "</span>";
        var rData = L.marker([mobiLocation], { icon: mobiIcon })
                .addTo(map)
                .bindPopup(mobiContent);
        return rData;
    }
}

function mapMobiLocE(eLoc) {
    if(isSet(eLoc.Location) && eLoc.Location !== "[,]") {
        var pTemp = conv2Tf(eLoc.BattTemp/10);
        var mCoordsJson = JSON.parse(eLoc.MobiLoc);
        var mobiLocation = mCoordsJson[1] + "," + mCoordsJson[0];
        if(mobiLocation === ',' || mobiLocation == 'null,null') {
            mobiLocation = getHomeGeo("geoJSONr");
        }
        // set session mobile location to mobiLocation
        var mobiContent = "Emily (S4)<br/>" +
                "Ping: " + (aLoc.WalkTime).substring(6, 10) + "<br/>" +
                "Battery: " + aLoc.BattLevel + "%<br/>" +
                "Temp: <span style='" + styleTemp(pTemp) + "'>" + pTemp + "</span>";
        var rData = L.marker([mobiLocation], { icon: mobiIcon })
                .addTo(map)
                .bindPopup(mobiContent);
        return rData;
    }
}