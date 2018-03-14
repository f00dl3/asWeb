/* 
by Anthony Stump
Created: 7 Mar 2018
Updated: 13 Mar 2018
 */

function color2Grad(type, direct, vals) {
    var st = "white", fc, fw, cMatcher = vals.v1;
    switch(type) {
        case "H":
            var vStr1 = colorRh(vals.v1), vStr2 = colorRh(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = colorRh(vals.v3); }
            switch(cMatcher) {
                case (cMatcher < 40): fc = "white"; fw = "normal"; break;
                case (cMatcher <= 40) && (cMatcher > 95): fc = "black"; fw = "normal"; break;
                case (cMatcher > 95): fc = "white"; fw = "bold"; break;
                default: fc = "black"; fw = "normal"; break;
            } break;
        case "T":
            var vStr1 = colorTemp(vals.v1), vStr2 = colorTemp(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = colorTemp(vals.v3); }
            switch(cMatcher) {
                case (cMatcher > -40) && (cMatcher < 25): fc = "white"; fw = "bold"; break;
                case (cMatcher > 94): fc = "white"; fw = "bold"; break;
                default: fc = "black"; fw = "normal"; break;
            } break;
        case "W":
            var vStr1 = colorWind(vals.v1), vStr2 = colorWind(vals.v2);
            if(isSet(vals.v3)) { var vStr3 = colorWind(vals.v3); }
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

function colorCape(cape) {	
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

function colorRh(rh) {
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

function colorTemp(tT) {
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

function colorWind(wMax) {
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

function conv2Mph(wsKt) {
	return round(wsKt*1.15078,0);
}

function conv2Tf(tCel) {
    if((isset(tCel)) && (tCel < 150)) {	
        return round((tCel * 9/5 + 32),0);
    } else {
        return '';
    }
}

function relativeHumidity(tF,dF) { 
    var tC = (5/9)*(tF-32);
    var dC = (5/9)*(dF-32);
    var satVapPres = 6.11 * Math.pow(10,(7.5 * tC / (237.7 + tC)));
    var actVapPres = 6.11 * Math.pow(10,(7.5 * dC / (237.7 + dC)));
    return round(((actVapPres/satVapPres) * 100),0);
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

        