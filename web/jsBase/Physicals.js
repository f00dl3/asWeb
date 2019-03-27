/*
 * by Anthony Stump
 * Created: 27 Mar 2019
 * Updated: on Creation
 */

function getPhysicalData() {
    aniPreload("on");
    var thePostData = { "doWhat": "getPhysical" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    putPhysicalData(data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for PhysicalData FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                }
            );
    });
}

function putPhysicalData(physDataset) {
	console.log(physDataset);
	var cols = [
		"Date",
		"<div class='UPop'>CHO<div class='UPopO'>Total Cholesterol</div></div>",
		"<div class='UPop'>CHL<div class='UPopO'>Cholesterol HDL</div></div>",
		"<div class='UPop'>CHR<div class='UPopO'>Cholesterol HDL Ratio</div></div>",
		"<div class='UPop'>CHN<div class='UPopO'>Cholesterol Non-HDL</div></div>",
		"<div class='UPop'>CLC<div class='UPopO'>Cholesterol LDL Calculated</div></div>",
		"<div class='UPop'>CVL<div class='UPopO'>Cholesterol VLDL</div></div>",
		"<div class='UPop'>GLU<div class='UPopO'>Glucose Level</div></div>",
		"<div class='UPop'>SOD<div class='UPopO'>Sodium</div></div>",
		"<div class='UPop'>CAL<div class='UPopO'>Calcium</div></div>",
		"<div class='UPop'>CRE<div class='UPopO'>Creatinine</div></div>",
		"<div class='UPop'>TRI<div class='UPopO'>Triglycerides</div></div>",
		"<div class='UPop'>IRO<div class='UPopO'>Iron</div></div>",
		"<div class='UPop'>AKP<div class='UPopO'>Alkiline Phosphorous</div></div>",
		"<div class='UPop'>ALB<div class='UPopO'>Albumin</div></div>",
		"<div class='UPop'>AGR<div class='UPopO'>Albumin Globulin Ratio</div></div>",
		"<div class='UPop'>GLO<div class='UPopO'>Globulin</div></div>",
		"<div class='UPop'>COR<div class='UPopO'>Chloride</div></div>",
		"<div class='UPop'>CO2<div class='UPopO'>CO2</div></div>",
		"<div class='UPop'>BUN<div class='UPopO'>BUN</div></div>",
		"<div class='UPop'>ALS<div class='UPopO'>ALT_SGPT</div></div>",
		"<div class='UPop'>ASS<div class='UPopO'>AST_SGOT</div></div>",
		"<div class='UPop'>GFR<div class='UPopO'>GFR Estimate</div></div>",
		"<div class='UPop'>GFA<div class='UPopO'>GFR Estimate African American</div></div>",
		"<div class='UPop'>ICT<div class='UPopO'>Icterus</div></div>",
		"<div class='UPop'>PLA<div class='UPopO'>Blood Plasma Count</div></div>",
		"<div class='UPop'>LIP<div class='UPopO'>Lipid Serum</div></div>",
		"<div class='UPop'>PRO<div class='UPopO'>T-Protein</div></div>",
		"<div class='UPop'>TBI<div class='UPopO'>Tbil</div></div>",
		"<div class='UPop'>TSH<div class='UPopO'>TSH</div></div>"
		];
	var rData = "<div class='table'>" +
		"<div class='tr'>";
	for(var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
	rData += "</div>";
	physDataset.forEach(function(pd) {
		rData += "<div class='tr'>" +
			"<span class='td'>" + pd.Date + "</span>" +
			"<span class='td'>" + pd.Cholesterol + "</span>" +
			"<span class='td'>" + pd.HDL + "</span>" +
			"<span class='td'>" + pd.Chol_HDL + "</span>" +
			"<span class='td'>" + pd.NonHDL_Chol + "</span>" +
			"<span class='td'>" + pd.LDL_Calc + "</span>" +
			"<span class='td'>" + pd.VLDL + "</span>" +
			"<span class='td'>" + pd.Glucose_Level + "</span>" +
			"<span class='td'>" + pd.Sodium + "</span>" +
			"<span class='td'>" + pd.Calcium + "</span>" +
			"<span class='td'>" + pd.Creatinine + "</span>" +
			"<span class='td'>" + pd.Triglycerides + "</span>" +
			"<span class='td'>" + pd.Iron + "</span>" +
			"<span class='td'>" + pd.AlkPhos + "</span>" +
			"<span class='td'>" + pd.Albumin + "</span>" +
			"<span class='td'>" + pd.AlbGlobRatio + "</span>" +
			"<span class='td'>" + pd.Globulin + "</span>" +
			"<span class='td'>" + pd.Chloride + "</span>" +
			"<span class='td'>" + pd.CO2 + "</span>" +
			"<span class='td'>" + pd.BUN + "</span>" +
			"<span class='td'>" + pd.ALT_SGPT + "</span>" +
			"<span class='td'>" + pd.AST_SGOT + "</span>" +
			"<span class='td'>" + pd.GFR_Est + "</span>" +
			"<span class='td'>" + pd.GFR_Est_AA + "</span>" +
			"<span class='td'>" + pd.Icterus + "</span>" +
			"<span class='td'>" + pd.K_Plasma + "</span>" +
			"<span class='td'>" + pd.Lipid_Serum + "</span>" +
			"<span class='td'>" + pd.T_Protein + "</span>" +
			"<span class='td'>" + pd.Tbil + "</span>" +
			"<span class='td'>" + pd.TSH + "</span>" +
			"</div>";
	});
	rData += "</div>";
	dojo.byId("physDataPlaceholder").innerHTML = rData;	
}

function initPhysicals() {
	getPhysicalData();
}

dojo.ready(initPhysicals);
