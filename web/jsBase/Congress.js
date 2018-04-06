/* 
by Anthony Stump
Created: 6 Apr 2018
 */

function getCongress() {
    aniPreload("on");
    var thePostData = "doWhat=getCongress";
    var xhArgs = {
        preventCache: true,
        url: getResource("Congress"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            showCongress(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Congress FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function showCongress(conData) {
    var conCols = [ "TH", "Name", "State", "Term", "Infos" ];
    var rData = "<h3>Congress Data</h3>";
    var conTable = "<div class='table'><div class='tr'>";
    for(var i = 0; i < conCols.length; i++) { conTable += "<span class='td'><strong>" + conCols[i] + "</strong></span>"; }
    conTable += "</div>";
    conData.forEach(function (cHak) {
        var tFlags, tHome, tCamp, tPwb;
        tHome = tCamp = tFlags = tPwb = "";
        var thisImage = getBasePath("congress") + "/" + cHak.last + "_" + cHak.first + ".jpg";
        conTable += "<div class='tr'>" +
                "<span class='td'>" +
                "<a href='" + thisImage + "' target='image'>" +
                "<img src='" + thisImage + "' class='th_icon'/></a>" +
                "</span>" +
                "<span class='td'><div class='UPop'>" + cHak.last + ", " + cHak.first +
                "<div class='UPopO'><img src='" + thisImage + "' class='th_medium'/><br/>" +
                "<strong>Nickname:</strong> " + cHak.nickname + "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + cHak.state;
        if(cHak.district !== 0) { conTable += "<div class='UPopO'>" + cHak.district + "</div>"; }
        conTable += "</div></span>";
        if(isSet(cHak.religion)) { tFlags += "<strong>Religion</strong>: " + cHak.religion + "<br/>"; }
        if(isSet(cHak.maritalStatus)) { tFlags += "<strong>Marital Status</strong>: " + cHak.maritalStatus + "<br/>"; }
        if(isSet(cHak.spouse)) { tFlags += "<strong>Spouse</strong>: " + cHak.spouse + "<br/>"; }
        if(isSet(cHak.agriculture)) { tFlags += "Agriculture<br/>"; }
        if(isSet(cHak.appropriations)) { tFlags += "Appropriations<br/>"; }
        if(isSet(cHak.appropsCardinals)) { tFlags += "Approps. Cardinals<br/>"; }
        if(isSet(cHak.armedServices)) { tFlags += "Armed Services<br/>"; }
        if(isSet(cHak.blDogs)) { tFlags += "Blue Dogs<br/>"; }
        if(isSet(cHak.board)) { tFlags += "Board<br/>"; }
        if(isSet(cHak.budget)) { tFlags += "Budget<br/>"; }
        if(isSet(cHak.CBC)) { tFlags += "CBC<br/>"; }
        if(isSet(cHak.CHAPAC)) { tFlags += "CHAPAC<br/>"; }
        if(isSet(cHak.CHC)) { tFlags += "CHC<br/>"; }
        if(isSet(cHak.educationWorkforce)) { tFlags += "Education/Workforce<br/>"; }
        if(isSet(cHak.energyCommerce)) { tFlags += "Energy/Commerce<br/>"; }
        if(isSet(cHak.ethicsSoOC)) { tFlags += "Ethics SoOC<br/>"; }
        if(isSet(cHak.freshman)) { tFlags += "Freshman<br/>"; }
        if(isSet(cHak.frontline)) { tFlags += "Frontline<br/>"; }
        if(isSet(cHak.foreignAffairsIR)) { tFlags += "Foreign Affairs/IR<br/>"; }
        if(isSet(cHak.homelandSecurity)) { tFlags += "Homeland Security<br/>"; }
        if(isSet(cHak.houseLeadership)) { tFlags += "House Leadership<br/>"; }
        if(isSet(cHak.houseAdministration)) { tFlags += "House Administration<br/>"; }
        if(isSet(cHak.judiciary)) { tFlags += "Judiciary<br/>"; }
        if(isSet(cHak.mentors)) { tFlags += "Mentors<br/>"; }
        if(isSet(cHak.naturalResources)) { tFlags += "Natural Resources<br/>"; }
        if(isSet(cHak.NDC)) { tFlags += "NDC<br/>"; }
        if(isSet(cHak.nonSpeciality)) { tFlags += "Non-Specialty<br/>"; }
        if(isSet(cHak.oversightGovtReform)) { tFlags += "Oversight Government Reform<br/>"; }
        if(isSet(cHak.proDem)) { tFlags += "Pro-Dem<br/>"; }
        if(isSet(cHak.rankingMember)) { tFlags += "Ranking Member<br/>"; }
        if(isSet(cHak.rules)) { tFlags += "Rules<br/>"; }
        if(isSet(cHak.science)) { tFlags += "Science<br/>"; }
        if(isSet(cHak.SEEC)) { tFlags += "SEEC<br/>"; }
        if(isSet(cHak.selectIntelligence)) { tFlags += "Select Intelligence<br/>"; }
        if(isSet(cHak.smallBusiness)) { tFlags += "Small Business<br/>"; }
        if(isSet(cHak.TEAMBuilders)) { tFlags += "TEAM Builders<br/>"; }
        if(isSet(cHak.TI)) { tFlags += "TI<br/>"; }
        if(isSet(cHak.VA)) { tFlags += "Veterans Affairs<br/>"; }
        if(isSet(cHak.waysMeans)) { tFlags += "Ways/Means<br/>"; }
        if(isSet(cHak.women)) { tFlags += "Women<br/>"; }
        if(isSet(cHak.memberCell)) { tHome += "<strong>Cell: </strong>" + cHak.memberCell + "<br/>"; }
        if(isSet(cHak.memberEmail)) { tHome += "<strong>Email: </strong>" + cHak.memberEmail + "<br/>"; }
        if(isSet(cHak.memberOtherPhone)) { tHome += "<strong>Other phone: </strong>" + cHak.memberOtherPhone + "<br/>"; }
        if(isSet(cHak.primaryHomeAddress)) { tHome += "<strong>Home Address: </strong>" + cHak.primaryHomeAddress + "<br/>"; }
        if(isSet(cHak.primaryHomePhone)) { tHome += "<strong>Home Phone: </strong>" + cHak.primaryHomePhone + "<br/>"; }
        if(isSet(cHak.campaignContact)) { tCamp += "<strong>Contact: </strong>" + cHak.campaignContact + "<br/>"; }
        if(isSet(cHak.campaignContactTitle)) { tCamp += "<strong>Contact Title: </strong>" + cHak.campaignContactTitle + "<br/>"; }
        if(isSet(cHak.campContactEmail)) { tCamp += "<strong>Email: </strong>" + cHak.campContactEmail + "<br/>"; }
        if(isSet(cHak.campContactPhone)) { tCamp += "<strong>Phone: </strong>" + cHak.campContactPhone + "<br/>"; }
        if(isSet(cHak.campOfficeFax)) { tCamp += "<strong>Fax: </strong>" + cHak.campOfficeFax + "<br/>"; }
        if(isSet(cHak.campOfficeAddress)) { tCamp += "<strong>Address: </strong>" + cHak.campOfficeAddress + "<br/>"; }
        if(isSet(cHak.PACWkBkUser)) { tPwb += "<strong>U: </strong>" + cHak.PACWkBkUser + "<br/>"; }
        if(isSet(cHak.PACWkBkPass)) { tPwb += "<strong>U: </strong>" + cHak.PACWkBkPass + "<br/>"; }
        conTable += "<span class='td'>" + cHak.term + "</span>" +
                "<span class='td'>";
        if(isSet(tFlags)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_lst.jpeg' class='th_icon'><div class='UPopO'>" + tFlags + "</div></div>"; }
        if(isSet(tHome)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_hom.gif' class='th_icon'><div class='UPopO'>" + tHome + "</div></div>"; }
        if(isSet(tCamp)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_off.jpeg' class='th_icon'><div class='UPopO'>" + tCamp + "</div></div>"; }
        if(isSet(tPwb)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_lck.jpeg' class='th_icon'><div class='UPopO'>" + tPwb + "</div></div>"; }
        conTable += "</span></div>";
    });
    conTable += "</div>";
    dojo.byId("CongressHolder").innerHTML = rData;
}

function init() {
    getCongress();
}

dojo.ready(init);


