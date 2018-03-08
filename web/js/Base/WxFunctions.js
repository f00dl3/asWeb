/* 
by Anthony Stump
Created: 7 Feb 2018
 */

function colorRh(rh) {
    var bg = "";
    var tc = "";
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
    var bg = "", tc = "", fw = "";
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

function color2Grad(type, direct, vals) {
    var st = "white",
        fc = "",
        fw = "",
        cMatcher = vals['v1'];
    switch(type) {
        case "H":
            var vStr1 = colorRh(vals['v1']),
                vStr2 = colorRh(vals['v2']);
            if(isSet(vals['v3'])) {
                var vStr3 = colorRh(vals['v3']);
            }
            switch(cMatcher) {
                case (cMatcher < 40): fc = "white"; fw = "normal"; break;
                case (cMatcher <= 40) && (cMatcher > 95): fc = "black"; fw = "normal"; break;
                case (cMatcher > 95): fc = "white"; fw = "bold"; break;
                default: fc = "black"; fw = "normal"; break;
            }
            break;
        case "T":
            var vStr1 = colorTemp(vals['v1']),
                vStr2 = colorTemp(vals['v2']);
            
    }
}