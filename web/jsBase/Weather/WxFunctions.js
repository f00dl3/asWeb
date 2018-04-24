/* 
by Anthony Stump
Created: 7 Mar 2018
Updated: 23 Apr 2018
 */

function color2Grad(type, direct, vals) {
    var st = "white", fc, fw, cMatcher = vals.v1;
    switch(type) {
        case "H":
            var vStr1 = styleRh(vals.v1), vStr2 = styleRh(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = styleRh(vals.v3); }
            switch(cMatcher) {
                case (cMatcher < 40): fc = "white"; fw = "normal"; break;
                case (cMatcher <= 40) && (cMatcher > 95): fc = "black"; fw = "normal"; break;
                case (cMatcher > 95): fc = "white"; fw = "bold"; break;
                default: fc = "black"; fw = "normal"; break;
            } break;
        case "T":
            var vStr1 = styleTemp(vals.v1), vStr2 = styleTemp(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = styleTemp(vals.v3); }
            switch(cMatcher) {
                case (cMatcher > -40) && (cMatcher < 25): fc = "white"; fw = "bold"; break;
                case (cMatcher > 94): fc = "white"; fw = "bold"; break;
                default: fc = "black"; fw = "normal"; break;
            } break;
        case "W":
            var vStr1 = styleWind(vals.v1), vStr2 = styleWind(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = styleWind(vals.v3); }
            switch(cMatcher) {
                case (cMatcher < 5): fc = "black"; st = fc; fw = "normal"; break;
                default: fc = "black"; st = "white"; fw = "normal"; break;
            } break;
        default: fc = "black"; st = "white"; fw = "normal"; break;
    }
    vStr1 = "#"+vStr1.split("#")[1].substring(0, 6);
    vStr2 = "#"+vStr2.split("#")[1].substring(0, 6);
    var style = "color: " + fc + ";" +
            " font-weight: " + fw + ";" +
            " text-align: center;" +
            " stroke: " + st +";" +
            " background: linear-gradient(to " + direct + ", " + vStr1 + ", " + vStr2;
    if(isSet(vStr3)) { 
        vStr3 = "#"+vStr3.split("#")[1].substring(0, 6);
        style += ", " + vStr3;
    }
    style += ");";
    return style;
            
}

function colorBrn(brn) { 
    switch(true) {
        case (brn < 1): return 'KIB20';
        case (brn < 5) && (brn >= 1): return 'KIB25';
        case (brn < 10) && (brn >= 5): return 'KIB30';
        case (brn < 15) && (brn >= 10): return 'KIB35';
        case (brn < 60) && (brn >= 15): return 'KIO35';
        case (brn < 80) && (brn >= 60): return 'KIB35';
        case (brn < 100) && (brn >= 80): return 'KIB30';
        case (brn < 140) && (brn >= 100): return 'KIB25';
        case (brn >= 140): return 'KIB20';
    }
}

function colorBShr(bs) {
	switch(true) {
		case (bs < 20): return 'KIB20';
		case (bs < 30) && (bs >= 20): return 'KIB25';
		case (bs < 60) && (bs >= 30): return 'KIB30';
		case (bs < 90) && (bs >= 60): return 'KIB35';
		case (bs >= 90): return 'KIO35';
	}
}

function colorCin(cin) {
	switch(true) {
		case (cin >= 500): return 'EHIL10';
		case (cin < 500) && (cin >= 250): return 'EHIL15';
		case (cin < 250) && (cin >= 200): return 'EHIL20';
		case (cin < 200) && (cin >= 125): return 'EHIL25';
		case (cin < 125) && (cin >= 75): return 'EHIL35';
		case (cin < 75) && (cin >= 25): return 'EHIL45';
		case (cin < 25) && (cin > 0): return 'EHIL70';
		case (cin == 0): return 'EHIOXX';
		default: return 'EHIL10';
	}
}

function colorClouds(ccText) {
    var cc = ccText.valueOf();
    switch(cc) {
        case 0: return "C6C10";
        case 1: return "C6C09";
        case 2: return "C6C08";
        case 3: return "C6C07";
        case 4: return "C6C06";
        case 5: return "C6C05";
        case 6: return "C6C04";
        case 7: return "C6C03";
        case 8: return "C6C02";
        case 9: return "C6C01";
        case 10: return "C6C10";
    }
}

function colorEhi(ehi) {
    switch(true) {
        case (ehi < 1.0): return 'EHIL10';
        case (ehi < 1.5) && (ehi >= 1.0): return 'EHIL15';
        case (ehi < 2.0) && (ehi >= 1.5): return 'EHIL20';
        case (ehi < 2.5) && (ehi >= 2.0): return 'EHIL25';
        case (ehi < 3.0) && (ehi >= 2.5): return 'EHIL30';
        case (ehi < 3.5) && (ehi >= 3.0): return 'EHIL35';
        case (ehi < 4.0) && (ehi >= 3.5): return 'EHIL40';
        case (ehi < 4.5) && (ehi >= 4.0): return 'EHIL45';
        case (ehi < 5.0) && (ehi >= 4.5): return 'EHIL50';
        case (ehi < 5.5) && (ehi >= 5.0): return 'EHIL55';
        case (ehi < 6.0) && (ehi >= 5.5): return 'EHIL60';
        case (ehi < 7.0) && (ehi >= 6.0): return 'EHIL70';
        case (ehi < 8.0) && (ehi >= 7.0): return 'EHIL80';
        case (ehi < 9.0) && (ehi >= 8.0): return 'EHIL90';
        case (ehi < 10.0) && (ehi >= 9.0): return 'EHILXX';
        case (ehi >= 10.0): return 'EHIOXX';
	}
}

function colorKi(ki) {
	switch(true) {
		case (ki < 20): return 'KIB20';
		case (ki < 25) && (ki >= 20): return 'KIB25';
		case (ki < 30) && (ki >= 25): return 'KIB30';
		case (ki < 35) && (ki >= 30): return 'KIB35';
		case (ki >= 35): return 'KIO35';
	}
}

function colorLapse(lapse) {
	switch(true) {
		case (lapse < 5.5): return 'LAPSEB55';
		case (lapse < 6.0) && (lapse >= 5.5): return 'LAPSEB60';
		case (lapse < 6.5) && (lapse >= 6.0): return 'LAPSEB65';
		case (lapse < 7.0) && (lapse >= 6.5): return 'LAPSEB70';
		case (lapse < 7.5) && (lapse >= 7.0): return 'LAPSEB75';
		case (lapse < 8.0) && (lapse >= 7.5): return 'LAPSEB80';
		case (lapse < 8.5) && (lapse >= 8.0): return 'LAPSEB85';
		case (lapse < 9.0) && (lapse >= 8.5): return 'LAPSEB90';
		case (lapse < 9.5) && (lapse >= 9.0): return 'LAPSEB95';
		case (lapse >= 9.5): return 'LAPSEO95';
	}
}

function colorLcl(lcl) {
    switch(true) {
        case (lcl <= 500): return 'LCLB0500';
        case (lcl <= 750) && (lcl > 500): return 'LCLB0750';
        case (lcl <= 1000) && (lcl > 750): return 'LCLB1000';
        case (lcl <= 1250) && (lcl > 1000): return 'LCLB1250';
        case (lcl <= 1500) && (lcl > 1250): return 'LCLB1500';
        case (lcl <= 1750) && (lcl > 1500): return 'LCLB1750';
        case (lcl <= 2000) && (lcl > 1750): return 'LCLB2000';
        case (lcl <= 2500) && (lcl > 2000): return 'LCLB2500';
        case (lcl <= 3000) && (lcl > 2500): return 'LCLB3000';
        case (lcl <= 3500) && (lcl > 3000): return 'LCLB3500';
        case (lcl <= 4000) && (lcl > 3500): return 'LCLB4000';
        case (lcl > 4000): return 'LCLO4000';
    }
}

function colorLi(li) {
    var bg, tc;
    switch(true) {
        case (li > 10): bg = 'cccccc'; tc = 'black'; break;
        case (li <= 10) && (li > 6): bg = '0000ff'; tc = 'white'; break;
        case (li <= 6) && (li > 3): bg = '003300'; tc = 'white'; break;
        case (li <= 3) && (li > 0): bg = 'ffff00'; tc = 'black'; break;
        case (li <= 0) && (li > -2): bg = 'ff6600'; tc = 'white'; break;
        case (li <= -2) && (li > -4): bg = 'ff0000'; tc = 'white'; break;
        case (li <= -4) && (li > -6): bg = '660000'; tc = 'white'; break;
        case (li <= -6): bg = 'ff00ff'; tc = 'white'; break;
    }
    return "background-color: #" + bg + "; color: " + tc + "; text-align: center;";
}

function colorMmr(mmr) {
    switch(true) {
        case (mmr < 9): return 'MMRB09';
        case (mmr < 11) && (mmr >= 9): return 'MMRB11';
        case (mmr < 13) && (mmr >= 11): return 'MMRB13';
        case (mmr < 15) && (mmr >= 13): return 'MMRB15';
        case (mmr < 17) && (mmr >= 15): return 'MMRB17';
        case (mmr < 20) && (mmr >= 17): return 'MMRB20';
        case (mmr > 20): return 'MMRO20';
    }
}

function colorMuvv(muvv) {
    switch(true) {
        case (muvv < 5): return 'MMRB09';
        case (muvv < 10) && (muvv >= 5): return 'MMRB11';
        case (muvv < 20) && (muvv >= 10): return 'MMRB13';
        case (muvv < 30) && (muvv >= 20): return 'MMRB15';
        case (muvv < 45) && (muvv >= 30): return 'MMRB17';
        case (muvv < 60) && (muvv >= 45): return 'MMRB20';
        case (muvv >= 60): return 'MMRO20';
    }
}

function colorTempDiff(tDiffText) {
    var tDiff = tDiffText.valueOf();
    switch(true) {
        case inRange(tDiff, 999, -35): return "C6DB35";
        case inRange(tDiff, -34, -30): return "C6DB30B34";
        case inRange(tDiff, -29, -25): return "C6DB25B29";
        case inRange(tDiff, -24, -20): return "C6DB20B24";
        case inRange(tDiff, -19, -15): return "C6DB15B19";
        case inRange(tDiff, -14, -10): return "C6DB10B14";
        case inRange(tDiff, -9, -5): return "C6DB05B09";
        case inRange(tDiff, -4, -1): return "C6DB01B04";
        case 0: return "C6DNone";
        case inRange(tDiff, 1, 4): return "C6D004001";
        case inRange(tDiff, 5, 9): return "C6D009005";
        case inRange(tDiff, 10, 14): return "C6D014010";
        case inRange(tDiff, 15, 19): return "C6D019015";
        case inRange(tDiff, 24, 20): return "C6D024020";
        case inRange(tDiff, 29, 25): return "C6D029025";
        case inRange(tDiff, 34, 30): return "C6D034030";
        case inRange(tDiff, 35, 999): return "C6D035";
    }
}

function conv2Mph(wsKt) {
	return Math.round(wsKt*1.15078);
}

function colorSnow(snText) {
    var sn = snText.valueOf();
    switch(sn) {
        case 0.1: return "C6STRA";
        case inRange(sn, 0.2, 0.4): return "C6SG02";
        case inRange(sn, 0.5, 0.9): return "C6SG05";
        case inRange(sn, 1.0, 2.9): return "C6SG10";
        case inRange(sn, 3.0, 4.9): return "C6SG30";
        case inRange(sn, 5.0, 7.9): return "C6SG50";
        case inRange(sn, 8.0, 99.9): return "CSG80";
        default: return "C6ZERO";
    }
}

function conv2Tf(tCel) {
    if((isSet(tCel)) && (tCel < 150)) {	
        return Math.round(tCel * 9/5 + 32);
    } else {
        return '';
    }
}

function heights2Elevations(what, order) {
    var heights = [
        1000,
        975, 950, 925, 900,
        875, 850, 825, 800,
        775, 750, 725, 700,
        675, 650, 625, 600,
        575, 550, 525, 500,
        475, 450, 425, 400,
        375, 350, 325, 300,
        275, 250, 225, 200,
        175, 150, 125, 100
    ];
    var h2eMap = [
        0.4,
        1.0, 1.7, 2.5, 3.2,
        4.0, 4.8, 5.6, 6.4,
        7.2, 8.1, 9.0, 9.9,
        10.8, 11.8, 12.8, 13.8,
        14.9, 16.0, 17.1, 18.2,
        19.5, 20.8, 22.2, 23.6,
        25.1, 26.6, 28.3, 30.1,
        31.9, 34.0, 36.2, 38.6,
        41.3, 44.3, 47.7, 51.8
    ];
    switch(what) {
        case "height": if(order === "t2b") { heights.reverse(); } return heights; break;
        case "elev": if(order === "t2b") { h2eMap.reverse(); } return h2eMap; break;
    }
}

function parseWxObs(stationData) {
    var obsData = {
        "TimeString": stationData.TimeString,
        "SfcT": stationData.Temperature, "H1000T": stationData.T1000,
        "H975T": conv2Tf(stationData.T975), "H950T": conv2Tf(stationData.T950),
        "H925T": conv2Tf(stationData.T925), "H900T": conv2Tf(stationData.T900),
        "H875T": conv2Tf(stationData.T875), "H850T": conv2Tf(stationData.T850),
        "H825T": conv2Tf(stationData.T825), "H800T": conv2Tf(stationData.T800),
        "H775T": conv2Tf(stationData.T775), "H750T": conv2Tf(stationData.T750),
        "H725T": conv2Tf(stationData.T725), "H700T": conv2Tf(stationData.T700),
        "H675T": conv2Tf(stationData.T675), "H650T": conv2Tf(stationData.T650),
        "H625T": conv2Tf(stationData.T625), "H600T": conv2Tf(stationData.T600),
        "H575T": conv2Tf(stationData.T575), "H550T": conv2Tf(stationData.T550),
        "H525T": conv2Tf(stationData.T525), "H500T": conv2Tf(stationData.T500),
        "H475T": conv2Tf(stationData.T475), "H450T": conv2Tf(stationData.T450),
        "H425T": conv2Tf(stationData.T425), "H400T": conv2Tf(stationData.T400),
        "H375T": conv2Tf(stationData.T375), "H350T": conv2Tf(stationData.T350),
        "H325T": conv2Tf(stationData.T325), "H300T": conv2Tf(stationData.T300),
        "H275T": conv2Tf(stationData.T275), "H250T": conv2Tf(stationData.T250),
        "H225T": conv2Tf(stationData.T225), "H200T": conv2Tf(stationData.T200),
        "H175T": conv2Tf(stationData.T175), "H150T": conv2Tf(stationData.T150),
        "H125T": conv2Tf(stationData.T125), "H100T": conv2Tf(stationData.T100),
        "SfcWS": stationData.WindSpeed, "H1000WS": conv2Mph(stationData.WS1000),
        "H975WS": conv2Mph(stationData.WS975),"H950WS": conv2Mph(stationData.WS950),
        "H925WS": conv2Mph(stationData.WS925), "H900WS": conv2Mph(stationData.WS900),
        "H875WS": conv2Mph(stationData.WS875), "H850WS": conv2Mph(stationData.WS850),
        "H825WS": conv2Mph(stationData.WS825), "H800WS": conv2Mph(stationData.WS800),
        "H775WS": conv2Mph(stationData.WS775), "H750WS": conv2Mph(stationData.WS750),
        "H725WS": conv2Mph(stationData.WS725), "H700WS": conv2Mph(stationData.WS700),
        "H675WS": conv2Mph(stationData.WS675), "H650WS": conv2Mph(stationData.WS650),
        "H625WS": conv2Mph(stationData.WS625), "H600WS": conv2Mph(stationData.WS600),
        "H575WS": conv2Mph(stationData.WS575), "H550WS": conv2Mph(stationData.WS550),
        "H525WS": conv2Mph(stationData.WS525), "H500WS": conv2Mph(stationData.WS500),
        "H475WS": conv2Mph(stationData.WS475), "H450WS": conv2Mph(stationData.WS450),
        "H425WS": conv2Mph(stationData.WS425), "H400WS": conv2Mph(stationData.WS400),
        "H375WS": conv2Mph(stationData.WS375), "H350WS": conv2Mph(stationData.WS350),
        "H325WS": conv2Mph(stationData.WS325), "H300WS": conv2Mph(stationData.WS300),
        "H275WS": conv2Mph(stationData.WS275), "H250WS": conv2Mph(stationData.WS250),
        "H225WS": conv2Mph(stationData.WS225), "H200WS": conv2Mph(stationData.WS200),
        "H175WS": conv2Mph(stationData.WS175), "H150WS": conv2Mph(stationData.WS150),
        "H125WS": conv2Mph(stationData.WS125), "H100WS": conv2Mph(stationData.WS100),
        "SfcWV": windDirSvg(stationData.WindDegrees), "H1000WV": windDirSvg(stationData.WD1000),
        "H975WV": windDirSvg(stationData.WD975), "H950WV": windDirSvg(stationData.WD950),
        "H925WV": windDirSvg(stationData.WD925), "H900WV": windDirSvg(stationData.WD900),
        "H875WV": windDirSvg(stationData.WD875), "H850WV": windDirSvg(stationData.WD850),
        "H825WV": windDirSvg(stationData.WD825), "H800WV": windDirSvg(stationData.WD800),
        "H775WV": windDirSvg(stationData.WD775), "H750WV": windDirSvg(stationData.WD750),
        "H725WV": windDirSvg(stationData.WD725), "H700WV": windDirSvg(stationData.WD700),
        "H675WV": windDirSvg(stationData.WD675), "H650WV": windDirSvg(stationData.WD650),
        "H625WV": windDirSvg(stationData.WD625), "H600WV": windDirSvg(stationData.WD600),
        "H575WV": windDirSvg(stationData.WD575), "H550WV": windDirSvg(stationData.WD550),
        "H525WV": windDirSvg(stationData.WD525), "H500WV": windDirSvg(stationData.WD500),
        "H475WV": windDirSvg(stationData.WD475), "H450WV": windDirSvg(stationData.WD450),
        "H425WV": windDirSvg(stationData.WD425), "H400WV": windDirSvg(stationData.WD400),
        "H375WV": windDirSvg(stationData.WD375), "H350WV": windDirSvg(stationData.WD350),
        "H325WV": windDirSvg(stationData.WD325), "H300WV": windDirSvg(stationData.WD300),
        "H275WV": windDirSvg(stationData.WD275), "H250WV": windDirSvg(stationData.WD250),
        "H225WV": windDirSvg(stationData.WD225), "H200WV": windDirSvg(stationData.WD200),
        "H175WV": windDirSvg(stationData.WD175), "H150WV": windDirSvg(stationData.WD150),
        "H125WV": windDirSvg(stationData.WD125), "H100WV": windDirSvg(stationData.WD100),
        "SfcH": relativeHumidity(stationData.Temperature, stationData.Dewpoint), "H1000H": relativeHumidity(conv2Tf(stationData.T1000), conv2Tf(stationData.D1000)),
        "H975H": relativeHumidity(conv2Tf(stationData.T975), conv2Tf(stationData.D975)), "H950H": relativeHumidity(conv2Tf(stationData.T950), conv2Tf(stationData.D950)),
        "H925H": relativeHumidity(conv2Tf(stationData.T925), conv2Tf(stationData.D925)), "H900H": relativeHumidity(conv2Tf(stationData.T900), conv2Tf(stationData.D900)),
        "H875H": relativeHumidity(conv2Tf(stationData.T875), conv2Tf(stationData.D875)), "H850H": relativeHumidity(conv2Tf(stationData.T850), conv2Tf(stationData.D850)),
        "H825H": relativeHumidity(conv2Tf(stationData.T825), conv2Tf(stationData.D825)), "H800H": relativeHumidity(conv2Tf(stationData.T800), conv2Tf(stationData.D800)),
        "H775H": relativeHumidity(conv2Tf(stationData.T775), conv2Tf(stationData.D775)), "H750H": relativeHumidity(conv2Tf(stationData.T750), conv2Tf(stationData.D750)),
        "H725H": relativeHumidity(conv2Tf(stationData.T725), conv2Tf(stationData.D725)), "H700H": relativeHumidity(conv2Tf(stationData.T700), conv2Tf(stationData.D700)),
        "H675H": relativeHumidity(conv2Tf(stationData.T675), conv2Tf(stationData.D675)), "H650H": relativeHumidity(conv2Tf(stationData.T650), conv2Tf(stationData.D650)),
        "H625H": relativeHumidity(conv2Tf(stationData.T625), conv2Tf(stationData.D625)), "H600H": relativeHumidity(conv2Tf(stationData.T600), conv2Tf(stationData.D600)),
        "H575H": relativeHumidity(conv2Tf(stationData.T575), conv2Tf(stationData.D575)), "H550H": relativeHumidity(conv2Tf(stationData.T550), conv2Tf(stationData.D550)),
        "H525H": relativeHumidity(conv2Tf(stationData.T525), conv2Tf(stationData.D525)), "H500H": relativeHumidity(conv2Tf(stationData.T500), conv2Tf(stationData.D500)),
        "H475H": relativeHumidity(conv2Tf(stationData.T475), conv2Tf(stationData.D475)), "H450H": relativeHumidity(conv2Tf(stationData.T450), conv2Tf(stationData.D450)),
        "H425H": relativeHumidity(conv2Tf(stationData.T425), conv2Tf(stationData.D425)), "H400H": relativeHumidity(conv2Tf(stationData.T400), conv2Tf(stationData.D400)),
        "H375H": relativeHumidity(conv2Tf(stationData.T375), conv2Tf(stationData.D375)), "H350H": relativeHumidity(conv2Tf(stationData.T350), conv2Tf(stationData.D350)),
        "H325H": relativeHumidity(conv2Tf(stationData.T325), conv2Tf(stationData.D325)), "H300H": relativeHumidity(conv2Tf(stationData.T300), conv2Tf(stationData.D300)),
        "H275H": relativeHumidity(conv2Tf(stationData.T275), conv2Tf(stationData.D275)), "H250H": relativeHumidity(conv2Tf(stationData.T250), conv2Tf(stationData.D250)),
        "H225H": relativeHumidity(conv2Tf(stationData.T225), conv2Tf(stationData.D225)), "H200H": relativeHumidity(conv2Tf(stationData.T200), conv2Tf(stationData.D200)),
        "H175H": relativeHumidity(conv2Tf(stationData.T175), conv2Tf(stationData.D175)), "H150H": relativeHumidity(conv2Tf(stationData.T150), conv2Tf(stationData.D150)),
        "H125H": relativeHumidity(conv2Tf(stationData.T125), conv2Tf(stationData.D125)), "H100H": relativeHumidity(conv2Tf(stationData.T100), conv2Tf(stationData.D100)),
        "SfcD": stationData.Dewpoint, "H1000D": conv2Tf(stationData.D1000),
        "H975D": conv2Tf(stationData.D975), "H950D": conv2Tf(stationData.D950),
        "H925D": conv2Tf(stationData.D925), "H900D": conv2Tf(stationData.D900),
        "H875D": conv2Tf(stationData.D875), "H850D": conv2Tf(stationData.D850),
        "H825D": conv2Tf(stationData.D825), "H800D": conv2Tf(stationData.D800),
        "H775D": conv2Tf(stationData.D775), "H750D": conv2Tf(stationData.D750),
        "H725D": conv2Tf(stationData.D725), "H700D": conv2Tf(stationData.D700),
        "H675D": conv2Tf(stationData.D675), "H650D": conv2Tf(stationData.D650),
        "H625D": conv2Tf(stationData.D625), "H600D": conv2Tf(stationData.D600),
        "H575D": conv2Tf(stationData.D575), "H550D": conv2Tf(stationData.D550),
        "H525D": conv2Tf(stationData.D525), "H500D": conv2Tf(stationData.D500),
        "H475D": conv2Tf(stationData.D475), "H450D": conv2Tf(stationData.D450),
        "H425D": conv2Tf(stationData.D425), "H400D": conv2Tf(stationData.D400),
        "H375D": conv2Tf(stationData.D375), "H350D": conv2Tf(stationData.D350),
        "H325D": conv2Tf(stationData.D325), "H300D": conv2Tf(stationData.D300),
        "H275D": conv2Tf(stationData.D275), "H250D": conv2Tf(stationData.D250),
        "H225D": conv2Tf(stationData.D225), "H200D": conv2Tf(stationData.D200),
        "H175D": conv2Tf(stationData.D175), "H150D": conv2Tf(stationData.D150),
        "H125D": conv2Tf(stationData.D125), "H100D": conv2Tf(stationData.D100)
    };
    return obsData;
}

function relativeHumidity(tF,dF) { 
    var tC = (5/9)*(tF-32);
    var dC = (5/9)*(dF-32);
    var satVapPres = 6.11 * Math.pow(10,(7.5 * tC / (237.7 + tC)));
    var actVapPres = 6.11 * Math.pow(10,(7.5 * dC / (237.7 + dC)));
    return Math.round((actVapPres/satVapPres) * 100);
}

function snowRatio(liqPrecip,tF) {
    var ratio;
    switch(tF) {
        case (tF >= 28) && (tF <= 34): ratio = 1.0; break;
        case (tF >= 20) && (tF < 28): ratio = 1.25; break;
        case (tF >= 15) && (tF < 20): ratio = 1.5; break;
        case (tF >= 10) && (tF < 15): ratio = 1.75; break;
        case (tF >= 0) && (tF < 10): ratio = 2.0; break;
        case (tF >= -20) && (tF < 0): ratio = 3.0; break;
        case (tF < -20): ratio = 6.0; break;
    }
    return ratio;
}

function styleCape(cape) {	
        var tc, bg;
	switch(true) {
		case (cape < 10): bg = "cccccc"; tc = "black"; break;
		case (cape < 100) && (cape >= 10): bg = "99ff66"; tc = "black"; break;
		case (cape < 250) && (cape >= 100): bg = "66ff33"; tc = "black"; break;
		case (cape < 500) && (cape >= 250): bg = "00ff00"; tc = "black"; break; 
		case (cape < 750) && (cape >= 500): bg = "003300"; tc = "white"; break;
		case (cape < 1000) && (cape >= 750): bg = "cccc00"; tc = "black"; break;
		case (cape < 1500) && (cape >= 1000): bg = "ffff00"; tc = "black"; break;
		case (cape < 2000) && (cape >= 1500): bg = "ff9900"; tc = "black"; break;
		case (cape < 2500) && (cape >= 2000): bg = "ff0000"; tc = "black"; break;
		case (cape < 3000) && (cape >= 2500): bg = "990000"; tc = "white"; break;
		case (cape < 3500) && (cape >= 3000): bg = "660000"; tc = "white"; break;
		case (cape < 4000) && (cape >= 3500): bg = "ff00ff"; tc = "white"; break;
		case (cape >= 4000): bg = "660099"; tc = "white"; break;
	}
	return "background-color: #" + bg + "; color: " + tc + "; text-align: center;";
}

function styleRh(rh) {
    var bg, tc;
    switch(true) {
        case (rh <= 5): bg = "ff0000"; tc = "white"; break;
        case (rh <= 10) && (rh > 5): bg = "990000"; tc = "white"; break;
        case (rh <= 20) && (rh > 10): bg = "330000"; tc = "white"; break;
        case (rh <= 30) && (rh > 20): bg = "000000"; tc = "white"; break;
        case (rh <= 40) && (rh > 30): bg = "333333"; tc = "white"; break;
        case (rh <= 50) && (rh > 40): bg = "666666"; tc = "white"; break;
        case (rh <= 60) && (rh > 50): bg = "999999"; tc = "black"; break;
        case (rh <= 70) && (rh > 60): bg = "cccccc"; tc = "black"; break;
        case (rh <= 80) && (rh > 70): bg = "ffffff"; tc = "black"; break;
        case (rh <= 90) && (rh > 80): bg = "66ffff"; tc = "black"; break;
        case (rh <= 95) && (rh > 90): bg = "00ffff"; tc = "black"; break;
        case (rh > 95): bg = "0000ff"; tc = "white"; break;
    }
    return "background-color: #" + bg + "; color: " + tc + "; text-align: center;";
}

function styleTemp(tT) {
    var bg, tc, fw;
    tT = Math.round(tT);
    switch(true) {
        case (tT <= -75): bg = "00ffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -70) && (tT >= -74): bg = "33ffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -65) && (tT >= -69): bg = "66ffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -60) && (tT >= -64): bg = "99ffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -55) && (tT >= -59): bg = "ccffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -50) && (tT >= -54): bg = "ffffff"; tc = "black"; fw = "bold"; break;
        case (tT <= -45) && (tT >= -49): bg = "cccccc"; tc = "black"; fw = "bold"; break;
        case (tT <= -40) && (tT >= -44): bg = "999999"; tc = "black"; fw = "bold"; break;
        case (tT <= -35) && (tT >= -39): bg = "666666"; tc = "white"; fw = "bold"; break;
        case (tT <= -30) && (tT >= -34): bg = "333333"; tc = "white"; fw = "bold"; break;
        case (tT <= -25) && (tT >= -29): bg = "000000"; tc = "white"; fw = "bold"; break;
        case (tT <= -20) && (tT >= -24): bg = "330033"; tc = "white"; fw = "bold"; break;
        case (tT <= -15) && (tT >= -19): bg = "660066"; tc = "white"; fw = "bold"; break;
        case (tT <= -10) && (tT >= -14): bg = "990066"; tc = "white"; fw = "bold"; break;
        case (tT <= -5) && (tT >= -9): bg = "cc0066"; tc = "white"; fw = "bold"; break;
        case (tT <= 0) && (tT >= -4): bg = "cc0099"; tc = "white"; fw = "bold"; break;
        case (tT <= 4) && (tT >= 1): bg = "cc33cc"; tc = "white"; fw = "bold"; break;
        case (tT <= 9) && (tT >= 5): bg = "9900cc"; tc = "white"; fw = "bold"; break;
        case (tT <= 14) && (tT >= 10): bg = "0000cc"; tc = "white"; fw = "bold"; break;
        case (tT <= 19) && (tT >= 15): bg = "0066ff"; tc = "white"; fw = "bold"; break;
        case (tT <= 24) && (tT >= 20): bg = "0099ff"; tc = "white"; fw = "bold"; break;
        case (tT <= 29) && (tT >= 25): bg = "00ccff"; tc = "black"; fw = "bold"; break;
        case (tT <= 32) && (tT >= 30): bg = "00ffff"; tc = "black"; fw = "bold"; break;
        case (tT <= 34) && (tT >= 33): bg = "00ffff"; tc = "black"; fw = "normal"; break;
        case (tT <= 39) && (tT >= 35): bg = "00ffcc"; tc = "black"; fw = "normal"; break;
        case (tT <= 44) && (tT >= 40): bg = "00ff99"; tc = "black"; fw = "normal"; break;
        case (tT <= 49) && (tT >= 45): bg = "00cc66"; tc = "black"; fw = "normal"; break;
        case (tT <= 54) && (tT >= 50): bg = "99ff66"; tc = "black"; fw = "normal"; break;
        case (tT <= 59) && (tT >= 55): bg = "ccff33"; tc = "black"; fw = "normal"; break;
        case (tT <= 64) && (tT >= 60): bg = "ffff00"; tc = "black"; fw = "normal"; break;
        case (tT <= 69) && (tT >= 65): bg = "ffcc00"; tc = "black"; fw = "normal"; break;
        case (tT <= 74) && (tT >= 70): bg = "ff9900"; tc = "black"; fw = "normal"; break;
        case (tT <= 79) && (tT >= 75): bg = "ff6600"; tc = "black"; fw = "normal"; break;
        case (tT <= 84) && (tT >= 80): bg = "ff3300"; tc = "black"; fw = "normal"; break;
        case (tT <= 89) && (tT >= 85): bg = "ff0000"; tc = "black"; fw = "normal"; break;
        case (tT <= 94) && (tT >= 90): bg = "cc0000"; tc = "white"; fw = "normal"; break;
        case (tT <= 99) && (tT >= 95): bg = "990000"; tc = "white"; fw = "bold"; break;
        case (tT <= 104) && (tT >= 100): bg = "990099"; tc = "white"; fw = "bold"; break;
        case (tT <= 109) && (tT >= 105): bg = "ff00ff"; tc = "white"; fw = "bold"; break;
        case (tT <= 114) && (tT >= 110): bg = "ffccff"; tc = "black"; fw = "bold"; break;
        case (tT >= 115): bg = "ffffff"; tc = "black"; fw = "bold"; break;
    }
    return "background-color: #" + bg + "; color: " + tc + "; font-weight: " + fw + "; text-align: center;";
}

function styleLiquid(liqText) {
    var liq = liqText.valueOf();
    var bg, tc;
    switch(liq) {
        case inRange(liq, 0.02, 0.09): bg = "99ff99"; tc = "black"; break;
        case inRange(liq, 0.10, 0.24): bg = "00ff00"; tc = "black"; break;
        case inRange(liq, 0.25, 0.49): bg = "00dd00"; tc = "black"; break;
        case inRange(liq, 0.50, 0.99): bg = "00cc00"; tc = "black"; break;
        case inRange(liq, 1.00, 1.99): bg = "008800"; tc = "white"; break;
        case inRange(liq, 2.00, 99.99): bg = "004400"; tc = "white"; break;
        default: bg = "363636"; tc = "black"; break;
    }
    return "background-color: #" + bg + "; color: " + tc + "; text-align: center;";
}

function styleWind(wMax) {
    var bg, tc, st, fw;
    switch(true) {
        case (wMax < 5): bg = "ffffff"; tc = "black"; st = tc; fw = "normal"; break;
        case (wMax <= 9.9) && (wMax >= 5): bg = "ccbb77"; tc = "black"; st = tc; fw = "normal"; break;
        case (wMax <= 19.9) && (wMax >= 10): bg = "cc8844"; tc = "black"; st = tc; fw = "normal"; break;
        case (wMax <= 29.9) && (wMax >= 20): bg = "cc7722"; tc = "black"; st = tc; fw = "normal"; break;
        case (wMax <= 39.9) && (wMax >= 30): bg = "cc6600"; tc = "black"; st = tc; fw = "normal"; break;
        case (wMax <= 49.9) && (wMax >= 40): bg = "994400"; tc = "white"; st = tc; fw = "normal"; break;
        case (wMax <= 57.9) && (wMax >= 50): bg = "990000"; tc = "white"; st = tc; fw = "normal"; break;
        case (wMax <= 64.9) && (wMax >= 58): bg = "ff0000"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 77.9) && (wMax >= 65): bg = "ff00ff"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 89.9) && (wMax >= 78): bg = "ff33ff"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 104.9) && (wMax >= 90): bg = "ff66ff"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 119.9) && (wMax >= 105): bg = "ff99ff"; tc = "black"; st = "white"; fw = "bold"; break;
        case (wMax <= 134.9) && (wMax >= 120): bg = "ffccff"; tc = "black"; st = "white"; fw = "bold"; break;
        case (wMax <= 149.9) && (wMax >= 135): bg = "99ffff"; tc = "black"; st = "white"; fw = "bold"; break;
        case (wMax <= 164.9) && (wMax >= 150): bg = "6666ff"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 179.9) && (wMax >= 165): bg = "3333ff"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax <= 194.9) && (wMax >= 180): bg = "3333cc"; tc = "white"; st = tc; fw = "bold"; break;
        case (wMax >= 195): bg = "0000ff"; tc = "white"; st = tc; fw = "bold"; break;
        default: bg = "2a2a2a"; tc = "black"; st = tc; fw = "normal"; break;
    }
    return "background-color: #" + bg + "; stroke: " + st + "; color: " + tc + "; font-weight: " + fw + "; text-align: center;";
}

function timeDay() {
    // Does not work properly.
    dojo.require("dojo.date");
    var now = new Date();
    var p1d = getDate("day", +1, "js");
    var sunset = new Date().sunset(getHomeGeo("lat"), getHomeGeo("lon"));
    var sunrise = new Date().sunrise(getHomeGeo("lat"), getHomeGeo("lon"));
    var toSunrise = dojo.date.compare(now, sunrise, "datetime");
    var toSunset = dojo.date.compare(p1d, sunset, "datetime");
    if(toSunrise === 1 && toSunset === 0) { isDaylight = 1; } else { isDaylight = 0; }
    return isDaylight;
}

function windDirSvg(tDir) {
    var retSvg = "<svg width=18 height=18>";
    var strokeProps = "stroke-width=1 stroke='black'";
    var stroke2Props = "stroke-width=2 stroke='black'";
    switch (true) {
	case (tDir < 11.25 || tDir > 348.75): /* N */
            retSvg += "<line x1=9 y1=0 x2=9 y2=18 " + strokeProps + "/>" +
                    "<line x1=9 y1=18 x2=3 y2=12 " + strokeProps + "/>" +
                    "<line x1=9 y1=18 x2=15 y2=12 " + strokeProps + "/>"; 
            break;
        case (tDir >= 11.25 && tDir < 33.75): /* NNE */
            retSvg += "<line x1=14 y1=0 x2=5 y2=18 " + strokeProps + "/>" +
                    " <line x1=5 y1=18 x2=2 y2=10 " + strokeProps + " />" +
                    "<line x1=5 y1=18 x2=13 y2=14 " + strokeProps + " />";
            break;
        case (tDir >= 33.75 && tDir < 56.25): /* NE */
            retSvg += "<line x1=0 y1=18 x2=18 y2=0 " + strokeProps + " />" +
                    "<line x1=0 y1=18 x2=0 y2=10 " + stroke2Props + " />" +
                    "<line x1=0 y1=18 x2=8 y2=18 " + stroke2Props + " />";
            break;
            case (tDir >= 56.25 && tDir < 78.75): /* ENE */
                    "<line x1=18 y1=5 x2=0 y2=14 " + strokeProps + " >/" +
                    "<line x1=0 y1=14 x2=3 y2=6 " + strokeProps + " >/" +
                    "<line x1=0 y1=14 x2=8 y2=16 " + strokeProps + " />";
            break;
        case (tDir >= 78.75 && tDir < 101.25): /* E */
                    "<line x1=0 y1=9 x2=18 y2=9 " + strokeProps + " >/" +
                    "<line x1=0 y1=9 x2=6 y2=3 " + strokeProps + " >/" +
                    "<line x1=0 y1=9 x2=6 y2=15 " + strokeProps + " />";
            break;
        case (tDir >= 101.25 && tDir < 123.75): /* ESE */
                    "<line x1=18 y1=14 x2=0 y2=5 " + strokeProps + " >/" +
                    "<line x1=0 y1=5 x2=6 y2=0 " + strokeProps + " >/" +
                    "<line x1=0 y1=5 x2=4 y2=14 " + strokeProps + " />";
            break;
        case (tDir >= 123.75 && tDir < 146.25): /* SE */
                    "<line x1=18 y1=18 x2=0 y2=0 " + strokeProps + " >/" +
                    "<line x1=0 y1=0 x2=8 y2=0 " + stroke2Props + " >/" +
                    "<line x1=0 y1=0 x2=0 y2=8 " + stroke2Props + " />";
            break;
        case (tDir >= 146.25 && tDir < 168.75): /* SSE */
                    "<line x1=14 y1=18 x2=5 y1=0 " + strokeProps + " >/" +
                    "<line x1=5 y1=0 x2=1 y2=6 " + strokeProps + " >/" +
                    "<line x1=5 y1=0 x2=12 y2=3 " + strokeProps + " />";
            break;
        case (tDir >= 168.75 && tDir < 191.25): /* S */
                    "<line x1=9 y1=18 x2=9 y2=0 " + strokeProps + " >/" +
                    "<line x1=9 y1=0 x2=3 y2=6 " + strokeProps + " >/" +
                    "<line x1=9 y1=0 x2=15 y2=6 " + strokeProps + " />";
            break;
        case (tDir >= 191.25 && tDir < 213.75): /* SSW */
                    "<line x1=5 y1=18 x2=14 y2=0 " + strokeProps + " >/" +
                    "<line x1=14 y1=0 x2=6 y2=3 " + strokeProps + " >/" +
                    "<line x1=14 y1=0 x2=18 y2=8 " + strokeProps + " />";
            break;
        case (tDir >= 213.75 && tDir < 236.25): /* SW */
                    "<line x1=0 y1=18 x2=18 y2=0 " + strokeProps + " >/" +
                    "<line x1=18 y1=0 x2=10 y2=0 " + stroke2Props + " >/" +
                    "<line x1=18 y1=0 x2=18 y2=8 " + stroke2Props + " />";
            break;
        case (tDir >= 236.25 && tDir < 258.75): /* WSW */
                    "<line x1=0 y1=14 x2=18 y2=5 " + strokeProps + " >/" +
                    "<line x1=18 y1=5 x2=10 y2=2 " + strokeProps + " >/" +
                    "<line x1=18 y1=5 x2=14 y2=16 " + strokeProps + " />";
            break;
        case (tDir >= 258.75 && tDir < 281.25): /* W */
                    "<line x1=18 y1=9 x2=0 y2=9 " + strokeProps + " >/" +
                    "<line x1=18 y1=9 x2=12 y2=3 " + strokeProps + " >/" +
                    "<line x1=18 y1=9 x2=12 y2=15 " + strokeProps + " />";
            break;
        case (tDir >= 281.25 && tDir < 303.75): /* WNW */
                    "<line x1=0 y1=5 x2=18 y2=14 " + strokeProps + " >/" +
                    "<line x1=18 y1=14 x2=12 y2=18 " + strokeProps + " >/" +
                    "<line x1=18 y1=14 x2=13 y2=6 " + strokeProps + " />";
            break;
        case (tDir >= 303.75 && tDir < 326.25): /* NW */
                    "<line x1=0 y1=0 x2=18 y2=18 " + strokeProps + " >/" +
                    "<line x1=18 y1=18 x2=10 y2=18 " + stroke2Props + " >/" +
                    "<line x1=18 y1=18 x2=18 y2=10 " + stroke2Props + " />";
            break;
        case (tDir >= 326.25 && tDir < 348.75): /* NNW */
                    "<line x1=5 y1=0 x2=14 y2=18 " + strokeProps + " >/" +
                    "<line x1=14 y1=18 x2=6 y2=14 " + strokeProps + " >/" +
                    "<line x1=14 y1=18 x2=17 y2=10 " + strokeProps + " />";
            break;
    }       
    retSvg += "</svg>";
    return retSvg;
}

function windDirTxt(tDir) {
    switch (true) {
        case (tDir < 11.25 || tDir > 348.75): return'N';
        case (tDir >= 11.25 && tDir < 33.75): return'NNE';
        case (tDir >= 33.75 && tDir < 56.25): return'NE';
        case (tDir >= 56.25 && tDir < 78.75): return'ENE';
        case (tDir >= 78.75 && tDir < 101.25): return'E';
        case (tDir >= 101.25 && tDir < 123.75): return'ESE';
        case (tDir >= 123.75 && tDir < 146.25): return'SE';
        case (tDir >= 146.25 && tDir < 168.75): return'SSE';
        case (tDir >= 168.75 && tDir < 191.25): return'S';
        case (tDir >= 191.25 && tDir < 213.75): return'SSW';
        case (tDir >= 213.75 && tDir < 236.25): return'SW';
        case (tDir >= 236.25 && tDir < 258.75): return'WSW';
        case (tDir >= 258.75 && tDir < 281.25): return'W';
        case (tDir >= 281.25 && tDir < 303.75): return'WNW';
        case (tDir >= 303.75 && tDir < 326.25): return'NW';
        case (tDir >= 326.25 && tDir < 348.75): return'NNW';
    }
}

function wxObs(doWhat, obsTime, temp, wind, humid, wxThis) {
    var factor, icon;
    switch(wxThis) {
        case 'A Few Clouds': factor = 4; icon = 'fa'; break;
        case 'A Few Clouds and Breezy': factor = 4; icon = 'wf'; break; 
        case 'A Few Clouds and Windy': factor = 4; icon = 'wf'; break; 
        case 'A Few Clouds with Haze': factor = 4; icon='hz'; break; 
        case 'Breezy': factor = 5; icon = 'wd'; break;
        case 'Clear': factor = 5; icon = 'cl'; break;
        case 'Fair': factor = 5; icon = 'cl'; break;
        case 'Fair and Breezy': factor = 5; icon = 'wd'; break; 
        case 'Fair and Windy': factor = 5; icon = 'wd'; break; 
        case 'Fair with Haze': factor = 5; icon = 'hz'; break;
        case 'Fog': factor = 0; icon = 'fg'; break;
        case 'Fog/Mist': case 'BR': factor = -1; icon = 'fg'; break;
        case 'Fog/Mist and Windy': factor = -1; icon = 'fg'; break;
        case 'Freezing Fog': factor = 0; icon = 'fg'; break;
        case 'Heavy Rain': factor = -5; icon = 'ra'; break;
        case 'Heavy Rain Fog/Mist': factor = -5; icon = 'ra'; break;
        case 'Heavy Rain Fog/Mist and Breezy': factor = -5; icon = 'ra'; break;
        case 'Heavy Rain Fog/Mist and Windy': factor = -5; icon = 'ra'; break;
        case 'Heavy Rain and Windy': factor = -5; icon = 'ra'; break;
        case 'Heavy Snow': factor = -4; icon = 'sn'; break;
        case 'Heavy Snow Freezing Fog': factor = -4; icon = 'sn'; break;
        case 'Light Drizzle': case '-DZ': factor = -1; icon = 'df'; break;  
        case 'Light Drizzle Fog/Mist': factor = -1; icon = 'df'; break;  
        case 'Light Freezing Rain': factor = -1; icon = 'zr'; break;
        case 'Light Freezing Rain and Breezy': factor = -1; icon = 'zr'; break;
        case 'Light Freezing Rain Fog/Mist': factor = -1; icon = 'zr'; break;
        case 'Light Freezing Rain Fog/Mist and Breezy': factor = -1; icon = 'zr'; break;
        case 'Light Rain' && '-RA': factor = -1; icon = 'sh'; break;
        case 'Light Rain and Breezy': factor = -1; icon = 'sh'; break;
        case 'Light Rain and Windy': factor = -1; icon = 'sh'; break;
        case 'Light Rain Fog': factor = -1; icon = 'lr'; break;
        case 'Light Rain Fog/Mist':  factor = -1; icon = 'lr'; break;
        case 'Light Rain Fog/Mist and Breezy': factor = -1; icon = 'lr'; break;
        case 'Light Snow': factor = -1; icon = 'sn'; break;
        case 'Light Snow and Breezy': factor = -1; icon = 'sn'; break; 
        case 'Light Snow and Windy': factor = -1; icon = 'sn'; break; 
        case 'Light Snow Fog/Mist': factor = -1; icon = 'sn'; break; 
        case 'Light Snow Fog/Mist and Breezy': factor = -1; icon = 'sn'; break; 
        case 'Light Snow Freezing Fog': factor = -1; icon = 'sn'; break;
        case 'Mostly Cloudy': factor = 2; icon = 'mc'; break;
        case 'Mostly Cloudy and Breezy':  factor = 2; icon = 'wd'; break; 
        case 'Mostly Cloudy with Haze': factor = 2; icon = 'hz'; break;
        case 'Overcast': factor = 1; icon = 'oc'; break;
        case 'Overcast and Breezy': factor = 1; icon = 'wo'; break;
        case 'Overcast and Windy': factor = 1; icon = 'wo'; break;
        case 'Overcast with Haze': factor = 1; icon = 'hz'; break;
        case 'Partly Cloudy': factor = 3; icon = 'pc'; break;
        case 'Partly Cloudy and Breezy': factor = 3; icon = 'wp'; break;
        case 'Partly Cloudy and Windy': factor = 3; icon = 'wp'; break;
        case 'Rain': factor = -3; icon = 'ra'; break;
        case 'Rain and Breezy': factor = -3; icon = 'ra'; break;
        case 'Rain and Windy': factor = -3; icon = 'ra'; break;
        case 'Rain Fog': factor = -3; icon = 'ra'; break;
        case 'Rain Fog/Mist': factor = -3; icon = 'ra'; break;
        case 'Rain Fog/Mist and Breezy': factor = -3; icon = 'ra'; break;
        case 'Sandstorm': case 'SS': factor = 0; icon = 'du'; break;
        case 'Snow Freezing Fog': factor = -3; icon = 'sn'; break; 
        case 'Thunderstorm': case 'TS': factor = -4; icon = 'ts'; break;
        case 'Thunderstorm Heavy Rain and Breezy': factor = -5; icon = 'ts'; break;
        case 'Thunderstorm Heavy Rain Fog/Mist': factor = -5; icon = 'ts'; break;
        case 'Thunderstorm Heavy Rain Fog and Breezy': factor = -5; icon = 'ts'; break;
        case 'Thunderstorm Heavy Rain Fog Squalls and Windy': factor = -5; icon = 'ts'; break;
        case 'Thunderstorm in Vicinity': factor = 0; icon = 'tv'; break;
        case 'Thunderstorm in Vicinity and Breezy': factor = 0; icon = 'tv'; break;
        case 'Thunderstorm in Vicinity Fog/Mist': factor = 0; icon = 'tv'; break;
        case 'Thunderstorm in Vicinity Heavy Rain Fog/Mist': factor = -5; icon='ts'; break;
        case 'Thunderstorm in Vicinity Light Rain': case 'TS VCSH': factor = -1; icon = 'th'; break;
        case 'Thunderstorm in Vicinity Light Rain and Windy': factor = -3; icon = 'th'; break;
        case 'Thunderstorm in Vicinity Light Rain Fog/Mist': factor = -1; icon = 'th'; break;
        case 'Thunderstorm in Vicinity Light Rain Fog/Mist and Breezy': factor = -3; icon = 'th'; break;
        case 'Thunderstorm in Vicinity Rain': factor = -3; icon='ts'; break;
        case 'Thunderstorm in Vicinity Rain Fog/Mist': factor = -3; icon='ts'; break;
        case 'Thunderstorm in Vicinity Rain Fog/Mist and Breezy': factor = -3; icon='ts'; break;
        case 'Thunderstorm Light Rain': factor = -1; icon = 'ts'; break;
        case 'Thunderstorm Light Rain Fog/Mist': factor = -1; icon = 'ts'; break;
        case 'Thunderstorm Light Rain Fog/Mist and Windy': factor = -1; icon = 'ts'; break;
        case 'Thunderstorm Rain Fog/Mist': factor = -3; icon = 'ts'; break;
        case 'Thunderstorm Rain Fog/Mist and Breezy': factor = -3; icon = 'ts'; break;
        case 'Unknown Precip': factor = -1; icon = 'uk'; break;
        case 'Unknown Precip and Breezy': factor = -1; icon = 'uk'; break;
        case 'Unknown Precip Fog/Mist': factor = -1; icon = 'uk'; break;
        default: factor = 0; icon = 'xx'; break;
    }  
    switch(doWhat) {      
        case "Feel":
            var feelsLike;
            switch(true) {
                case(temp <= 60):
                    feelsLike = (wind === 0) ? temp : 35.74 + (0.6215 * temp) - (35.75 * Math.pow(wind, 0.16)) + (0.4275 * temp * Math.pow(wind, 0.16));
                    break;
                case(temp > 60 && temp < 70):
                    if(wind >= 20) { 
                        feelsLike = temp - 10;
                    } else {
                        feelsLike = temp - (wind * 0.5);
                    }
                    break;
                case (temp >= 70):
                    feelsLike = -42.379 + (2.04901523 * temp) + (10.14333127 * humid) -
                            (0.22475541 * temp * humid) - (6.83783 * Math.pow(10, -3) * Math.pow(temp, 2)) -
                            (5.481717 * Math.pow(10, -2) * Math.pow(humid, 2)) +
                            (1.22874 * Math.pow(10, -3) * Math.pow(temp, 2) * humid) +
                            (8.5282 * Math.pow(10, -4) * temp * Math.pow(humid, 2)) -
                            (1.99 * Math.pow(10, -6) * Math.pow(temp, 2) * Math.pow(humid, 2));
                    if(wind >= 20) {
                        feelsLike = feelsLike - 10;
                    } else {
                        feelsLike = feelsLike - (wind * 0.5);
                    }
                    break;
                default: feelsLike = temp; break;
            }
            if (timeDay(obsTime) === 0 && factor > 0) { factor = 0; }
            var finalFactor = factor + Math.round(feelsLike);
            return finalFactor;
            break;        
        case "Icon":
            if (timeDay(obsTime) === 0 && icon === 'cl') { icon = 'ncl'; }
            if (timeDay(obsTime) === 0 && icon === 'fa') { icon = 'nfa'; }
            if (timeDay(obsTime) === 0 && icon === 'fg') { icon = 'nfg'; }
            if (timeDay(obsTime) === 0 && icon === 'mc') { icon = 'nmc'; }
            if (timeDay(obsTime) === 0 && icon === 'mx') { icon = 'nmx'; }
            if (timeDay(obsTime) === 0 && icon === 'oc') { icon = 'noc'; }
            if (timeDay(obsTime) === 0 && icon === 'pc') { icon = 'npc'; }
            if (timeDay(obsTime) === 0 && icon === 'ra') { icon = 'nra'; }
            if (timeDay(obsTime) === 0 && icon === 'sh') { icon = 'nsh'; }
            if (timeDay(obsTime) === 0 && icon === 'sn') { icon = 'nsn'; }
            if (timeDay(obsTime) === 0 && icon === 'th') { icon = 'nth'; }
            if (timeDay(obsTime) === 0 && icon === 'ts') { icon = 'nts'; }
            if (timeDay(obsTime) === 0 && icon === 'uk') { icon = 'nuk'; }
            if (timeDay(obsTime) === 0 && icon === 'wd') { icon = 'nwd'; }
            return icon;
            break;                  
    }
}

function wxShortTime(inString) {
    var shortString = inString.replace(/Last\ Updated\ on\ /g, '');
    return shortString;
}