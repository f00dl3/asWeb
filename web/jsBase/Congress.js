/* 
by Anthony Stump
Created: 6 Apr 2018
Updated: 7 Apr 2018
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
        var tFlags, tHome, tCamp, tPwb, xTerm;
        tHome = tCamp = tFlags = tPwb = "";
        var thisImage = getBasePath("congress") + "/" + cHak.Last + "_" + cHak.First + ".jpg";
        conTable += "<div class='tr'>" +
                "<span class='td'>" +
                "<a href='" + thisImage + "' target='image'>" +
                "<img src='" + thisImage + "' class='th_icon'/></a>" +
                "</span>" +
                "<span class='td'><div class='UPop'>" + cHak.Last + ", " + cHak.First +
                "<div class='UPopO'><img src='" + thisImage + "' class='th_medium'/><br/>" +
                "<strong>Nickname:</strong> " + cHak.Nickname + "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + cHak.State;
        if(cHak.district !== 0) { conTable += "<div class='UPopO'>" + cHak.District + "</div>"; }
        if(isSet(cHak.Term)) { xTerm = cHak.Term; } else { xTerm = "?"; }
        conTable += "</div></span>";
        if(isSet(cHak.Religion)) { tFlags += "<strong>Religion</strong>: " + cHak.Religion + "<br/>"; }
        if(isSet(cHak.Marital_Status)) { tFlags += "<strong>Marital Status</strong>: " + cHak.Marital_Status + "<br/>"; }
        if(isSet(cHak.Spouse)) { tFlags += "<strong>Spouse</strong>: " + cHak.Spouse + "<br/>"; }
        if(isSet(cHak.Agriculture)) { tFlags += "Agriculture<br/>"; }
        if(isSet(cHak.Appropriations)) { tFlags += "Appropriations<br/>"; }
        if(isSet(cHak.Approps_Cardinals)) { tFlags += "Approps. Cardinals<br/>"; }
        if(isSet(cHak.Armed_Services)) { tFlags += "Armed Services<br/>"; }
        if(isSet(cHak.Bl_Dogs)) { tFlags += "Blue Dogs<br/>"; }
        if(isSet(cHak.Board)) { tFlags += "Board<br/>"; }
        if(isSet(cHak.Budget)) { tFlags += "Budget<br/>"; }
        if(isSet(cHak.CBC)) { tFlags += "CBC<br/>"; }
        if(isSet(cHak.CHAPAC)) { tFlags += "CHAPAC<br/>"; }
        if(isSet(cHak.CHC)) { tFlags += "CHC<br/>"; }
        if(isSet(cHak.Education_Workforce)) { tFlags += "Education/Workforce<br/>"; }
        if(isSet(cHak.Energy_Commerce)) { tFlags += "Energy/Commerce<br/>"; }
        if(isSet(cHak.Ethics_SoOC)) { tFlags += "Ethics SoOC<br/>"; }
        if(isSet(cHak.Freshman)) { tFlags += "Freshman<br/>"; }
        if(isSet(cHak.Frontline)) { tFlags += "Frontline<br/>"; }
        if(isSet(cHak.Foreign_Affairs_IR)) { tFlags += "Foreign Affairs/IR<br/>"; }
        if(isSet(cHak.Homeland_Security)) { tFlags += "Homeland Security<br/>"; }
        if(isSet(cHak.House_Leadership)) { tFlags += "House Leadership<br/>"; }
        if(isSet(cHak.House_Administration)) { tFlags += "House Administration<br/>"; }
        if(isSet(cHak.Judiciary)) { tFlags += "Judiciary<br/>"; }
        if(isSet(cHak.Mentors)) { tFlags += "Mentors<br/>"; }
        if(isSet(cHak.Natural_Resources)) { tFlags += "Natural Resources<br/>"; }
        if(isSet(cHak.NDC)) { tFlags += "NDC<br/>"; }
        if(isSet(cHak.Non_Speciality)) { tFlags += "Non-Specialty<br/>"; }
        if(isSet(cHak.Oversight_Govt_Reform)) { tFlags += "Oversight Government Reform<br/>"; }
        if(isSet(cHak.Pro_Dem)) { tFlags += "Pro-Dem<br/>"; }
        if(isSet(cHak.Ranking_Member)) { tFlags += "Ranking Member<br/>"; }
        if(isSet(cHak.Rules)) { tFlags += "Rules<br/>"; }
        if(isSet(cHak.Science)) { tFlags += "Science<br/>"; }
        if(isSet(cHak.SEEC)) { tFlags += "SEEC<br/>"; }
        if(isSet(cHak.Select_Intelligence)) { tFlags += "Select Intelligence<br/>"; }
        if(isSet(cHak.Small_Business)) { tFlags += "Small Business<br/>"; }
        if(isSet(cHak.TEAM_Builders)) { tFlags += "TEAM Builders<br/>"; }
        if(isSet(cHak.TI)) { tFlags += "TI<br/>"; }
        if(isSet(cHak.VA)) { tFlags += "Veterans Affairs<br/>"; }
        if(isSet(cHak.Ways_Means)) { tFlags += "Ways/Means<br/>"; }
        if(isSet(cHak.Women)) { tFlags += "Women<br/>"; }
        if(isSet(cHak.Member_Cell)) { tHome += "<strong>Cell: </strong>" + cHak.Member_Cell + "<br/>"; }
        if(isSet(cHak.Member_Email)) { tHome += "<strong>Email: </strong>" + cHak.Member_Email + "<br/>"; }
        if(isSet(cHak.Member_Other_Phone)) { tHome += "<strong>Other phone: </strong>" + cHak.Member_Other_Phone + "<br/>"; }
        if(isSet(cHak.Primary_Home_Address)) { tHome += "<strong>Home Address: </strong>" + cHak.Primary_Home_Address + "<br/>"; }
        if(isSet(cHak.Primary_Home_Phone)) { tHome += "<strong>Home Phone: </strong>" + cHak.Primary_Home_Phone + "<br/>"; }
        if(isSet(cHak.Campaign_Contact)) { tCamp += "<strong>Contact: </strong>" + cHak.Campaign_Contact + "<br/>"; }
        if(isSet(cHak.Campaign_Contact_Title)) { tCamp += "<strong>Contact Title: </strong>" + cHak.Campaign_Contact_Title + "<br/>"; }
        if(isSet(cHak.Camp_Contact_Email)) { tCamp += "<strong>Email: </strong>" + cHak.Camp_Contact_Email + "<br/>"; }
        if(isSet(cHak.Camp_Contact_Phone)) { tCamp += "<strong>Phone: </strong>" + cHak.Camp_Contact_Phone + "<br/>"; }
        if(isSet(cHak.Camp_Office_Fax)) { tCamp += "<strong>Fax: </strong>" + cHak.Camp_Office_Fax + "<br/>"; }
        if(isSet(cHak.Camp_Office_Address)) { tCamp += "<strong>Address: </strong>" + cHak.Camp_Office_Address + "<br/>"; }
        if(isSet(cHak.PACWkBk_User)) { tPwb += "<strong>U: </strong>" + cHak.PACWkBk_User + "<br/>"; }
        if(isSet(cHak.PACWkBk_Pass)) { tPwb += "<strong>U: </strong>" + cHak.PACWkBk_Pass + "<br/>"; }
        conTable += "<span class='td'>" + xTerm + "</span>" +
                "<span class='td'>";
        if(isSet(tFlags)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_lst.jpeg' class='th_icon'><div class='UPopO'>" + tFlags + "</div></div>"; }
        if(isSet(tHome)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_hom.gif' class='th_icon'><div class='UPopO'>" + tHome + "</div></div>"; }
        if(isSet(tCamp)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_off.jpeg' class='th_icon'><div class='UPopO'>" + tCamp + "</div></div>"; }
        if(isSet(tPwb)) { conTable += "<div class='UPop'> <img src='" + getBasePath("icon") + "/ic_lck.jpeg' class='th_icon'><div class='UPopO'>" + tPwb + "</div></div>"; }
        conTable += "</span></div>";
    });
    conTable += "</div>";
    rData += conTable;
    dojo.byId("CongressHolder").innerHTML = rData;
}

function init() {
    getCongress();
}

dojo.ready(init);


