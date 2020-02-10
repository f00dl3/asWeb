/*
by Anthony Stump
Created: 10 May 2019
Updated: 8 Feb 2020
*/

package asUtilsPorts.Weather;

import java.util.ArrayList;

public class Grib2Iterators {
    
    private ArrayList<Integer> heights(String detail) {
        ArrayList<Integer> heights = new ArrayList<Integer>();
        heights.add(0);
        switch(detail) {
            case "L": for(int i = 100; i <= 1000; i=i+50) { heights.add(i); } break;
            case "H": for(int i = 100; i <= 1000; i=i+25) { heights.add(i); } break;
        }
        return heights;
    }

	private double parseGrib2Data(String lineIn, int gribSpot, boolean doOffset) throws ArrayIndexOutOfBoundsException {
		double valueBack = 0.0;
		String[] lineTmp = lineIn.split(",");
		valueBack = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""));
                if(doOffset) { valueBack = valueBack-273.15; }
                return valueBack;
	}	
        
	private double parseGrib2DataD(String lineIn, int gribSpot, boolean doOffset) throws ArrayIndexOutOfBoundsException {
		double valueBack = parseGrib2Data(lineIn, gribSpot, doOffset);
		System.out.println(" --> HIT, parse [ " + lineIn.substring(0, 25) + " ] @ gribSpot [" + gribSpot + "] returned [ " + valueBack + " ]");
                return valueBack;
	}	
        
    private int xCmc(String paramIn) {
        final int sTC = 23;
        final int sWU = 43;
        final int sWV = 63;
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "TC1000": iR = sTC + 0; break;
            case "TC100": iR = sTC + 1; break;
            case "TC150": iR = sTC + 2; break;
            case "TC200": iR = sTC + 3; break;
            case "TC250": iR = sTC + 4; break;
            case "TC300": iR = sTC + 5; break;
            case "TC350": iR = sTC + 6; break;
            case "TC400": iR = sTC + 7; break;
            case "TC450": iR = sTC + 8; break;
            case "TC500": iR = sTC + 9; break;
            case "TC550": iR = sTC + 10; break;
            case "TC600": iR = sTC + 11; break;
            case "TC650": iR = sTC + 12; break;
            case "TC700": iR = sTC + 13; break;
            case "TC750": iR = sTC + 14; break;
            case "TC800": iR = sTC + 15; break;
            case "TC850": iR = sTC + 16; break;
            case "TC900": iR = sTC + 17; break;
            case "TC950": iR = sTC + 18; break;
            case "TC0": iR = sTC + 19; break;
            case "WU1000": iR = sWU + 0; break;
            case "WU100": iR = sWU + 1; break;
            case "WU150": iR = sWU + 2; break;
            case "WU200": iR = sWU + 3; break;
            case "WU250": iR = sWU + 4; break;
            case "WU300": iR = sWU + 5; break;
            case "WU350": iR = sWU + 6; break;
            case "WU400": iR = sWU + 7; break;
            case "WU450": iR = sWU + 8; break;
            case "WU500": iR = sWU + 9; break;
            case "WU550": iR = sWU + 10; break;
            case "WU600": iR = sWU + 11; break;
            case "WU650": iR = sWU + 12; break;
            case "WU700": iR = sWU + 13; break;
            case "WU750": iR = sWU + 14; break;
            case "WU800": iR = sWU + 15; break;
            case "WU850": iR = sWU + 16; break;
            case "WU900": iR = sWU + 17; break;
            case "WU950": iR = sWU + 18; break;
            case "WU0": iR = sWU + 19; break;
            case "WV1000": iR = sWV + 0; break;
            case "WV100": iR = sWV + 1; break;
            case "WV150": iR = sWV + 2; break;
            case "WV200": iR = sWV + 3; break;
            case "WV250": iR = sWV + 4; break;
            case "WV300": iR = sWV + 5; break;
            case "WV350": iR = sWV + 6; break;
            case "WV400": iR = sWV + 7; break;
            case "WV450": iR = sWV + 8; break;
            case "WV500": iR = sWV + 9; break;
            case "WV550": iR = sWV + 10; break;
            case "WV600": iR = sWV + 11; break;
            case "WV650": iR = sWV + 12; break;
            case "WV700": iR = sWV + 13; break;
            case "WV750": iR = sWV + 14; break;
            case "WV800": iR = sWV + 15; break;
            case "WV850": iR = sWV + 16; break;
            case "WV900": iR = sWV + 17; break;
            case "WV950": iR = sWV + 18; break;
            case "WV0": iR = sWV + 19; break;
            case "PRATE": iR = 1; break;
            case "HGT500": iR = 12; break;
        }
        return iR;
    }
        
    private int xGfs(String paramIn) {
        final int iSx = 1;
        final int iSs = 4;
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "TC100": iR = (iSx+0)+(iSs*0); break;
            case "TC150": iR = (iSx+0)+(iSs*1); break;
            case "TC200": iR = (iSx+0)+(iSs*2); break;
            case "TC250": iR = (iSx+0)+(iSs*3); break;
            case "TC300": iR = (iSx+0)+(iSs*4); break;
            case "TC350": iR = (iSx+0)+(iSs*5); break;
            case "TC400": iR = (iSx+0)+(iSs*6); break;
            case "TC450": iR = (iSx+0)+(iSs*7); break;
            case "TC500": iR = (iSx+0)+(iSs*8)+1; break;
            case "TC550": iR = (iSx+0)+(iSs*9)+1; break;
            case "TC600": iR = (iSx+0)+(iSs*10)+1; break;
            case "TC650": iR = (iSx+0)+(iSs*11)+1; break;
            case "TC700": iR = (iSx+0)+(iSs*12)+1; break;
            case "TC750": iR = (iSx+0)+(iSs*13)+1; break;
            case "TC800": iR = (iSx+0)+(iSs*14)+1; break;
            case "TC850": iR = (iSx+0)+(iSs*15)+1; break;
            case "TC900": iR = (iSx+0)+(iSs*16)+1; break;
            case "TC950": iR = (iSx+0)+(iSs*17)+1; break;
            case "TC1000": iR = (iSx+0)+(iSs*18)+1; break;
            case "TC0": iR = (iSx+0)+(iSs*19)+1; break;
            case "RH100": iR = (iSx+1)+(iSs*0); break;
            case "RH150": iR = (iSx+1)+(iSs*1); break;
            case "RH200": iR = (iSx+1)+(iSs*2); break;
            case "RH250": iR = (iSx+1)+(iSs*3); break;
            case "RH300": iR = (iSx+1)+(iSs*4); break;
            case "RH350": iR = (iSx+1)+(iSs*5); break;
            case "RH400": iR = (iSx+1)+(iSs*6); break;
            case "RH450": iR = (iSx+1)+(iSs*7); break;
            case "RH500": iR = (iSx+1)+(iSs*8)+1; break;
            case "RH550": iR = (iSx+1)+(iSs*9)+1; break;
            case "RH600": iR = (iSx+1)+(iSs*10)+1; break;
            case "RH650": iR = (iSx+1)+(iSs*11)+1; break;
            case "RH700": iR = (iSx+1)+(iSs*12)+1; break;
            case "RH750": iR = (iSx+1)+(iSs*13)+1; break;
            case "RH800": iR = (iSx+1)+(iSs*14)+1; break;
            case "RH850": iR = (iSx+1)+(iSs*15)+1; break;
            case "RH900": iR = (iSx+1)+(iSs*16)+1; break;
            case "RH950": iR = (iSx+1)+(iSs*17)+1; break;
            case "RH1000": iR = (iSx+1)+(iSs*18)+1; break;
            case "RH0": iR = (iSx+1)+(iSs*19)+1; break;
            case "WU100": iR = (iSx+3)+(iSs*0); break;
            case "WU150": iR = (iSx+3)+(iSs*1); break;
            case "WU200": iR = (iSx+3)+(iSs*2); break;
            case "WU250": iR = (iSx+3)+(iSs*3); break;
            case "WU300": iR = (iSx+3)+(iSs*4); break;
            case "WU350": iR = (iSx+3)+(iSs*5); break;
            case "WU400": iR = (iSx+3)+(iSs*6); break;
            case "WU450": iR = (iSx+3)+(iSs*7); break;
            case "WU500": iR = (iSx+3)+(iSs*8)+1; break;
            case "WU550": iR = (iSx+3)+(iSs*9)+1; break;
            case "WU600": iR = (iSx+3)+(iSs*10)+1; break;
            case "WU650": iR = (iSx+3)+(iSs*11)+1; break;
            case "WU700": iR = (iSx+3)+(iSs*12)+1; break;
            case "WU750": iR = (iSx+3)+(iSs*13)+1; break;
            case "WU800": iR = (iSx+3)+(iSs*14)+1; break;
            case "WU850": iR = (iSx+3)+(iSs*15)+1; break;
            case "WU900": iR = (iSx+3)+(iSs*16)+1; break;
            case "WU950": iR = (iSx+3)+(iSs*17)+1; break;
            case "WU1000": iR = (iSx+3)+(iSs*18)+1; break;
            case "WU0": iR = (iSx+3)+(iSs*19)+1; break;
            case "WV100": iR = (iSx+4)+(iSs*0); break;
            case "WV150": iR = (iSx+4)+(iSs*1); break;
            case "WV200": iR = (iSx+4)+(iSs*2); break;
            case "WV250": iR = (iSx+4)+(iSs*3); break;
            case "WV300": iR = (iSx+4)+(iSs*4); break;
            case "WV350": iR = (iSx+4)+(iSs*5); break;
            case "WV400": iR = (iSx+4)+(iSs*6); break;
            case "WV450": iR = (iSx+4)+(iSs*7); break;
            case "WV500": iR = (iSx+4)+(iSs*8)+1; break;
            case "WV550": iR = (iSx+4)+(iSs*9)+1; break;
            case "WV600": iR = (iSx+4)+(iSs*10)+1; break;
            case "WV650": iR = (iSx+4)+(iSs*11)+1; break;
            case "WV700": iR = (iSx+4)+(iSs*12)+1; break;
            case "WV750": iR = (iSx+4)+(iSs*13)+1; break;
            case "WV800": iR = (iSx+4)+(iSs*14)+1; break;
            case "WV850": iR = (iSx+4)+(iSs*15)+1; break;
            case "WV900": iR = (iSx+4)+(iSs*16)+1; break;
            case "WV950": iR = (iSx+4)+(iSs*17)+1; break;
            case "WV1000": iR = (iSx+4)+(iSs*18)+1; break;
            case "WV0": iR = (iSx+4)+(iSs*19)+1; break;
            case "CAPE": iR = 84; break;
            case "CIN": iR = 85; break;
            case "PRATE": iR = 82; break;
            case "PWAT": iR = 86; break;
            case "LI": iR = 83; break;
            case "HGT500": iR = 33; break;                
        }
        return iR;
    }
	
	private int xHrrr(String paramIn) {
        final int iSx = 31; /* Relative Humidity Offset */
        final int iSs = 14;
        int iR = 0;
        switch (paramIn.toUpperCase()) {
            case "RH100": iR = (iSx+0)+(iSs*0); break;
            case "RH125": iR = (iSx+0)+(iSs*1); break;
            case "RH150": iR = (iSx+0)+(iSs*2); break;
            case "RH175": iR = (iSx+0)+(iSs*3); break;
            case "RH200": iR = (iSx+0)+(iSs*4); break;
            case "RH225": iR = (iSx+0)+(iSs*5); break;
            case "RH250": iR = (iSx+0)+(iSs*6); break;
            case "RH275": iR = (iSx+0)+(iSs*7); break;
            case "RH300": iR = (iSx+0)+(iSs*8); break;
            case "RH325": iR = (iSx+0)+(iSs*9); break;
            case "RH350": iR = (iSx+0)+(iSs*10); break;
            case "RH375": iR = (iSx+0)+(iSs*11); break;
            case "RH400": iR = (iSx+0)+(iSs*12); break;
            case "RH425": iR = (iSx+0)+(iSs*13); break;
            case "RH450": iR = (iSx+0)+(iSs*14); break;
            case "RH475": iR = (iSx+0)+(iSs*15); break;
            case "RH500": iR = (iSx+0)+(iSs*16); break;
            case "RH525": iR = (iSx+0)+(iSs*17); break;
            case "RH550": iR = (iSx+0)+(iSs*18); break;
            case "RH575": iR = (iSx+0)+(iSs*19); break;
            case "RH600": iR = (iSx+0)+(iSs*20); break;
            case "RH625": iR = (iSx+0)+(iSs*21); break;
            case "RH650": iR = (iSx+0)+(iSs*22); break;
            case "RH675": iR = (iSx+0)+(iSs*23); break;
            case "RH700": iR = (iSx+0)+(iSs*24); break;
            case "RH725": iR = (iSx+0)+(iSs*25); break;
            case "RH750": iR = (iSx+0)+(iSs*26); break;
            case "RH775": iR = (iSx+0)+(iSs*27); break;
            case "RH800": iR = (iSx+0)+(iSs*28); break;
            case "RH825": iR = (iSx+0)+(iSs*29); break;
            case "RH850": iR = (iSx+0)+(iSs*30); break;
            case "RH875": iR = (iSx+0)+(iSs*31); break;
            case "RH900": iR = (iSx+0)+(iSs*32); break;
            case "RH925": iR = (iSx+0)+(iSs*33); break;
            case "RH950": iR = (iSx+0)+(iSs*34); break;
            case "RH975": iR = (iSx+0)+(iSs*35); break;
            case "RH1000": iR = 534; break;
            case "RH0": iR = 609; break;
            case "TC100": iR = (iSx-1)+(iSs*0); break;
            case "TC125": iR = (iSx-1)+(iSs*1); break;
            case "TC150": iR = (iSx-1)+(iSs*2); break;
            case "TC175": iR = (iSx-1)+(iSs*3); break;
            case "TC200": iR = (iSx-1)+(iSs*4); break;
            case "TC225": iR = (iSx-1)+(iSs*5); break;
            case "TC250": iR = (iSx-1)+(iSs*6); break;
            case "TC275": iR = (iSx-1)+(iSs*7); break;
            case "TC300": iR = (iSx-1)+(iSs*8); break;
            case "TC325": iR = (iSx-1)+(iSs*9); break;
            case "TC350": iR = (iSx-1)+(iSs*10); break;
            case "TC375": iR = (iSx-1)+(iSs*11); break;
            case "TC400": iR = (iSx-1)+(iSs*12); break;
            case "TC425": iR = (iSx-1)+(iSs*13); break;
            case "TC450": iR = (iSx-1)+(iSs*14); break;
            case "TC475": iR = (iSx-1)+(iSs*15); break;
            case "TC500": iR = (iSx-1)+(iSs*16); break;
            case "TC525": iR = (iSx-1)+(iSs*17); break;
            case "TC550": iR = (iSx-1)+(iSs*18); break;
            case "TC575": iR = (iSx-1)+(iSs*19); break;
            case "TC600": iR = (iSx-1)+(iSs*20); break;
            case "TC625": iR = (iSx-1)+(iSs*21); break;
            case "TC650": iR = (iSx-1)+(iSs*22); break;
            case "TC675": iR = (iSx-1)+(iSs*23); break;
            case "TC700": iR = (iSx-1)+(iSs*24); break;
            case "TC725": iR = (iSx-1)+(iSs*25); break;
            case "TC750": iR = (iSx-1)+(iSs*26); break;
            case "TC775": iR = (iSx-1)+(iSs*27); break;
            case "TC800": iR = (iSx-1)+(iSs*28); break;
            case "TC825": iR = (iSx-1)+(iSs*29); break;
            case "TC850": iR = (iSx-1)+(iSs*30); break;
            case "TC875": iR = (iSx-1)+(iSs*31); break;
            case "TC900": iR = (iSx-1)+(iSs*32); break;
            case "TC925": iR = (iSx-1)+(iSs*33); break;
            case "TC950": iR = (iSx-1)+(iSs*34); break;
            case "TC975": iR = (iSx-1)+(iSs*35); break;
            case "TC1000": iR = 533; break;
            case "TC0": iR = 605; break;
            case "WU100": iR = (iSx+4)+(iSs*0); break;
            case "WU125": iR = (iSx+4)+(iSs*1); break;
            case "WU150": iR = (iSx+4)+(iSs*2); break;
            case "WU175": iR = (iSx+4)+(iSs*3); break;
            case "WU200": iR = (iSx+4)+(iSs*4); break;
            case "WU225": iR = (iSx+4)+(iSs*5); break;
            case "WU250": iR = (iSx+4)+(iSs*6); break;
            case "WU275": iR = (iSx+4)+(iSs*7); break;
            case "WU300": iR = (iSx+4)+(iSs*8); break;
            case "WU325": iR = (iSx+4)+(iSs*9); break;
            case "WU350": iR = (iSx+4)+(iSs*10); break;
            case "WU375": iR = (iSx+4)+(iSs*11); break;
            case "WU400": iR = (iSx+4)+(iSs*12); break;
            case "WU425": iR = (iSx+4)+(iSs*13); break;
            case "WU450": iR = (iSx+4)+(iSs*14); break;
            case "WU475": iR = (iSx+4)+(iSs*15); break;
            case "WU500": iR = (iSx+4)+(iSs*16); break;
            case "WU525": iR = (iSx+4)+(iSs*17); break;
            case "WU550": iR = (iSx+4)+(iSs*18); break;
            case "WU575": iR = (iSx+4)+(iSs*19); break;
            case "WU600": iR = (iSx+4)+(iSs*20); break;
            case "WU625": iR = (iSx+4)+(iSs*21); break;
            case "WU650": iR = (iSx+4)+(iSs*22); break;
            case "WU675": iR = (iSx+4)+(iSs*23); break;
            case "WU700": iR = (iSx+4)+(iSs*24); break;
            case "WU725": iR = (iSx+4)+(iSs*25); break;
            case "WU750": iR = (iSx+4)+(iSs*26); break;
            case "WU775": iR = (iSx+4)+(iSs*27); break;
            case "WU800": iR = (iSx+4)+(iSs*28); break;
            case "WU825": iR = (iSx+4)+(iSs*29); break;
            case "WU850": iR = (iSx+4)+(iSs*30); break;
            case "WU875": iR = (iSx+4)+(iSs*31); break;
            case "WU900": iR = (iSx+4)+(iSs*32); break;
            case "WU925": iR = (iSx+4)+(iSs*33); break;
            case "WU950": iR = (iSx+4)+(iSs*34); break;
            case "WU975": iR = (iSx+4)+(iSs*35); break;
            case "WU1000": iR = 538; break;
            case "WU0": iR = 610; break;
            case "WV100": iR = (iSx+5)+(iSs*0); break;
            case "WV125": iR = (iSx+5)+(iSs*1); break;
            case "WV150": iR = (iSx+5)+(iSs*2); break;
            case "WV175": iR = (iSx+5)+(iSs*3); break;
            case "WV200": iR = (iSx+5)+(iSs*4); break;
            case "WV225": iR = (iSx+5)+(iSs*5); break;
            case "WV250": iR = (iSx+5)+(iSs*6); break;
            case "WV275": iR = (iSx+5)+(iSs*7); break;
            case "WV300": iR = (iSx+5)+(iSs*8); break;
            case "WV325": iR = (iSx+5)+(iSs*9); break;
            case "WV350": iR = (iSx+5)+(iSs*10); break;
            case "WV375": iR = (iSx+5)+(iSs*11); break;
            case "WV400": iR = (iSx+5)+(iSs*12); break;
            case "WV425": iR = (iSx+5)+(iSs*13); break;
            case "WV450": iR = (iSx+5)+(iSs*14); break;
            case "WV475": iR = (iSx+5)+(iSs*15); break;
            case "WV500": iR = (iSx+5)+(iSs*16); break;
            case "WV525": iR = (iSx+5)+(iSs*17); break;
            case "WV550": iR = (iSx+5)+(iSs*18); break;
            case "WV575": iR = (iSx+5)+(iSs*19); break;
            case "WV600": iR = (iSx+5)+(iSs*20); break;
            case "WV625": iR = (iSx+5)+(iSs*21); break;
            case "WV650": iR = (iSx+5)+(iSs*22); break;
            case "WV675": iR = (iSx+5)+(iSs*23); break;
            case "WV700": iR = (iSx+5)+(iSs*24); break;
            case "WV725": iR = (iSx+5)+(iSs*25); break;
            case "WV750": iR = (iSx+5)+(iSs*26); break;
            case "WV775": iR = (iSx+5)+(iSs*27); break;
            case "WV800": iR = (iSx+5)+(iSs*28); break;
            case "WV825": iR = (iSx+5)+(iSs*29); break;
            case "WV850": iR = (iSx+5)+(iSs*30); break;
            case "WV875": iR = (iSx+5)+(iSs*31); break;
            case "WV900": iR = (iSx+5)+(iSs*32); break;
            case "WV925": iR = (iSx+5)+(iSs*33); break;
            case "WV950": iR = (iSx+5)+(iSs*34); break;
            case "WV975": iR = (iSx+5)+(iSs*35); break;
            case "WV1000": iR = 539; break;
            case "WV0": iR = 611; break;
            case "CAPE": iR = 677; break;
            case "CIN": iR = 676; break;
            case "LI": iR = 633; break;
            case "PWAT": iR = 636; break;
            case "HGT500": iR = 253; break;
        }
        return iR;
    }
        
    private int xHrrrB(String paramIn) {
        final int iSx = 1;
        final int iSs = 5;
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "TC100": iR = (iSx+0)+(iSs*0); break;
            case "TC150": iR = (iSx+0)+(iSs*1); break;
            case "TC200": iR = (iSx+0)+(iSs*2); break;
            case "TC250": iR = (iSx+0)+(iSs*3); break;
            case "TC300": iR = (iSx+0)+(iSs*4); break;
            case "TC350": iR = (iSx+0)+(iSs*5); break;
            case "TC400": iR = (iSx+0)+(iSs*6); break;
            case "TC450": iR = (iSx+0)+(iSs*7); break;
            case "TC500": iR = (iSx+0)+(iSs*8)+1; break;
            case "TC550": iR = (iSx+0)+(iSs*9)+1; break;
            case "TC600": iR = (iSx+0)+(iSs*10)+1; break;
            case "TC650": iR = (iSx+0)+(iSs*11)+1; break;
            case "TC700": iR = (iSx+0)+(iSs*12)+1; break;
            case "TC750": iR = (iSx+0)+(iSs*13)+1; break;
            case "TC800": iR = (iSx+0)+(iSs*14)+1; break;
            case "TC850": iR = (iSx+0)+(iSs*15)+1; break;
            case "TC900": iR = (iSx+0)+(iSs*16)+1; break;
            case "TC950": iR = (iSx+0)+(iSs*17)+1; break;
            case "TC1000": iR = (iSx+0)+(iSs*18)+1; break;
            case "TC0": iR = 98; break;
            case "RH100": iR = (iSx+1)+(iSs*0); break;
            case "RH150": iR = (iSx+1)+(iSs*1); break;
            case "RH200": iR = (iSx+1)+(iSs*2); break;
            case "RH250": iR = (iSx+1)+(iSs*3); break;
            case "RH300": iR = (iSx+1)+(iSs*4); break;
            case "RH350": iR = (iSx+1)+(iSs*5); break;
            case "RH400": iR = (iSx+1)+(iSs*6); break;
            case "RH450": iR = (iSx+1)+(iSs*7); break;
            case "RH500": iR = (iSx+1)+(iSs*8)+1; break;
            case "RH550": iR = (iSx+1)+(iSs*9)+1; break;
            case "RH600": iR = (iSx+1)+(iSs*10)+1; break;
            case "RH650": iR = (iSx+1)+(iSs*11)+1; break;
            case "RH700": iR = (iSx+1)+(iSs*12)+1; break;
            case "RH750": iR = (iSx+1)+(iSs*13)+1; break;
            case "RH800": iR = (iSx+1)+(iSs*14)+1; break;
            case "RH850": iR = (iSx+1)+(iSs*15)+1; break;
            case "RH900": iR = (iSx+1)+(iSs*16)+1; break;
            case "RH950": iR = (iSx+1)+(iSs*17)+1; break;
            case "RH1000": iR = (iSx+1)+(iSs*18)+1; break;
            case "RH0": iR = 100; break;
            case "WU100": iR = (iSx+3)+(iSs*0); break;
            case "WU150": iR = (iSx+3)+(iSs*1); break;
            case "WU200": iR = (iSx+3)+(iSs*2); break;
            case "WU250": iR = (iSx+3)+(iSs*3); break;
            case "WU300": iR = (iSx+3)+(iSs*4); break;
            case "WU350": iR = (iSx+3)+(iSs*5); break;
            case "WU400": iR = (iSx+3)+(iSs*6); break;
            case "WU450": iR = (iSx+3)+(iSs*7); break;
            case "WU500": iR = (iSx+3)+(iSs*8)+1; break;
            case "WU550": iR = (iSx+3)+(iSs*9)+1; break;
            case "WU600": iR = (iSx+3)+(iSs*10)+1; break;
            case "WU650": iR = (iSx+3)+(iSs*11)+1; break;
            case "WU700": iR = (iSx+3)+(iSs*12)+1; break;
            case "WU750": iR = (iSx+3)+(iSs*13)+1; break;
            case "WU800": iR = (iSx+3)+(iSs*14)+1; break;
            case "WU850": iR = (iSx+3)+(iSs*15)+1; break;
            case "WU900": iR = (iSx+3)+(iSs*16)+1; break;
            case "WU950": iR = (iSx+3)+(iSs*17)+1; break;
            case "WU1000": iR = (iSx+3)+(iSs*18)+1; break;
            case "WU0": iR = 101; break;
            case "WV100": iR = (iSx+4)+(iSs*0); break;
            case "WV150": iR = (iSx+4)+(iSs*1); break;
            case "WV200": iR = (iSx+4)+(iSs*2); break;
            case "WV250": iR = (iSx+4)+(iSs*3); break;
            case "WV300": iR = (iSx+4)+(iSs*4); break;
            case "WV350": iR = (iSx+4)+(iSs*5); break;
            case "WV400": iR = (iSx+4)+(iSs*6); break;
            case "WV450": iR = (iSx+4)+(iSs*7); break;
            case "WV500": iR = (iSx+4)+(iSs*8)+1; break;
            case "WV550": iR = (iSx+4)+(iSs*9)+1; break;
            case "WV600": iR = (iSx+4)+(iSs*10)+1; break;
            case "WV650": iR = (iSx+4)+(iSs*11)+1; break;
            case "WV700": iR = (iSx+4)+(iSs*12)+1; break;
            case "WV750": iR = (iSx+4)+(iSs*13)+1; break;
            case "WV800": iR = (iSx+4)+(iSs*14)+1; break;
            case "WV850": iR = (iSx+4)+(iSs*15)+1; break;
            case "WV900": iR = (iSx+4)+(iSs*16)+1; break;
            case "WV950": iR = (iSx+4)+(iSs*17)+1; break;
            case "WV1000": iR = (iSx+4)+(iSs*18)+1; break;
            case "WV0": iR = 102; break;
            case "CAPE": iR = 105; break;
            case "CIN": iR = 106; break;
            case "PRATE": iR = 103; break;
            case "PWAT": iR = 107; break;
            case "LI": iR = 109; break;
            case "HGT500": iR = 41; break;                
        }
        return iR;
    }
    
    private int xHrwX(String paramIn) {
        final int iSx = 11;
        final int iSs = 3;
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "TC100": iR = (iSx+0)+(iSs*0); break;
            case "TC150": iR = (iSx+0)+(iSs*1); break;
            case "TC200": iR = (iSx+0)+(iSs*2); break;
            case "TC250": iR = (iSx+0)+(iSs*3); break;
            case "TC300": iR = (iSx+0)+(iSs*4); break;
            case "TC350": iR = (iSx+0)+(iSs*5); break;
            case "TC400": iR = (iSx+0)+(iSs*6); break;
            case "TC450": iR = (iSx+0)+(iSs*7); break;
            case "TC500": iR = (iSx+0)+(iSs*8)+1; break;
            case "TC550": iR = (iSx+0)+(iSs*9)+1; break;
            case "TC600": iR = (iSx+0)+(iSs*10)+1; break;
            case "TC650": iR = (iSx+0)+(iSs*11)+1; break;
            case "TC700": iR = (iSx+0)+(iSs*12)+1; break;
            case "TC750": iR = (iSx+0)+(iSs*13)+1; break;
            case "TC800": iR = (iSx+0)+(iSs*14)+1; break;
            case "TC850": iR = (iSx+0)+(iSs*15)+1; break;
            case "TC900": iR = (iSx+0)+(iSs*16)+1; break;
            case "TC950": iR = (iSx+0)+(iSs*17)+1; break;
            case "TC1000": iR = (iSx+0)+(iSs*18)+1; break;
            case "TC0": iR = 1; break;
            case "RH100": iR = (iSx+1)+(iSs*0); break;
            case "RH150": iR = (iSx+1)+(iSs*1); break;
            case "RH200": iR = (iSx+1)+(iSs*2); break;
            case "RH250": iR = (iSx+1)+(iSs*3); break;
            case "RH300": iR = (iSx+1)+(iSs*4); break;
            case "RH350": iR = (iSx+1)+(iSs*5); break;
            case "RH400": iR = (iSx+1)+(iSs*6); break;
            case "RH450": iR = (iSx+1)+(iSs*7); break;
            case "RH500": iR = (iSx+1)+(iSs*8)+1; break;
            case "RH550": iR = (iSx+1)+(iSs*9)+1; break;
            case "RH600": iR = (iSx+1)+(iSs*10)+1; break;
            case "RH650": iR = (iSx+1)+(iSs*11)+1; break;
            case "RH700": iR = (iSx+1)+(iSs*12)+1; break;
            case "RH750": iR = (iSx+1)+(iSs*13)+1; break;
            case "RH800": iR = (iSx+1)+(iSs*14)+1; break;
            case "RH850": iR = (iSx+1)+(iSs*15)+1; break;
            case "RH900": iR = (iSx+1)+(iSs*16)+1; break;
            case "RH950": iR = (iSx+1)+(iSs*17)+1; break;
            case "RH1000": iR = (iSx+1)+(iSs*18)+1; break;
            case "RH0": iR = 2; break;
            case "WU100": iR = (iSx+2)+(iSs*0); break;
            case "WU150": iR = (iSx+2)+(iSs*1); break;
            case "WU200": iR = (iSx+2)+(iSs*2); break;
            case "WU250": iR = (iSx+2)+(iSs*3); break;
            case "WU300": iR = (iSx+2)+(iSs*4); break;
            case "WU350": iR = (iSx+2)+(iSs*5); break;
            case "WU400": iR = (iSx+2)+(iSs*6); break;
            case "WU450": iR = (iSx+2)+(iSs*7); break;
            case "WU500": iR = (iSx+2)+(iSs*8)+1; break;
            case "WU550": iR = (iSx+2)+(iSs*9)+1; break;
            case "WU600": iR = (iSx+2)+(iSs*10)+1; break;
            case "WU650": iR = (iSx+2)+(iSs*11)+1; break;
            case "WU700": iR = (iSx+2)+(iSs*12)+1; break;
            case "WU750": iR = (iSx+2)+(iSs*13)+1; break;
            case "WU800": iR = (iSx+2)+(iSs*14)+1; break;
            case "WU850": iR = (iSx+2)+(iSs*15)+1; break;
            case "WU900": iR = (iSx+2)+(iSs*16)+1; break;
            case "WU950": iR = (iSx+2)+(iSs*17)+1; break;
            case "WU1000": iR = (iSx+2)+(iSs*18)+1; break;
            case "WU0": iR = 3; break;
            case "CAPE": iR = 4; break;
            case "CIN": iR = 5; break;
            case "PRATE": iR = 9; break;
            case "PWAT": iR = 6; break;
            case "LI": iR = 10; break;
            case "HGT500": iR = 29; break;
        }
        return iR;
    }
    
    private int xNam(String paramIn) {
        final int iSx = 1;
        final int iSs = 3;
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "TC100": iR = (iSx+0)+(iSs*0); break;
            case "TC150": iR = (iSx+0)+(iSs*1); break;
            case "TC200": iR = (iSx+0)+(iSs*2); break;
            case "TC250": iR = (iSx+0)+(iSs*3); break;
            case "TC300": iR = (iSx+0)+(iSs*4); break;
            case "TC350": iR = (iSx+0)+(iSs*5); break;
            case "TC400": iR = (iSx+0)+(iSs*6); break;
            case "TC450": iR = (iSx+0)+(iSs*7); break;
            case "TC500": iR = (iSx+0)+(iSs*8)+1; break;
            case "TC550": iR = (iSx+0)+(iSs*9)+1; break;
            case "TC600": iR = (iSx+0)+(iSs*10)+1; break;
            case "TC650": iR = (iSx+0)+(iSs*11)+1; break;
            case "TC700": iR = (iSx+0)+(iSs*12)+1; break;
            case "TC750": iR = (iSx+0)+(iSs*13)+1; break;
            case "TC800": iR = (iSx+0)+(iSs*14)+1; break;
            case "TC850": iR = (iSx+0)+(iSs*15)+1; break;
            case "TC900": iR = (iSx+0)+(iSs*16)+1; break;
            case "TC950": iR = (iSx+0)+(iSs*17)+1; break;
            case "TC1000": iR = (iSx+0)+(iSs*18)+1; break;
            case "TC0": iR = 60; break;
            case "RH100": iR = (iSx+1)+(iSs*0); break;
            case "RH150": iR = (iSx+1)+(iSs*1); break;
            case "RH200": iR = (iSx+1)+(iSs*2); break;
            case "RH250": iR = (iSx+1)+(iSs*3); break;
            case "RH300": iR = (iSx+1)+(iSs*4); break;
            case "RH350": iR = (iSx+1)+(iSs*5); break;
            case "RH400": iR = (iSx+1)+(iSs*6); break;
            case "RH450": iR = (iSx+1)+(iSs*7); break;
            case "RH500": iR = (iSx+1)+(iSs*8)+1; break;
            case "RH550": iR = (iSx+1)+(iSs*9)+1; break;
            case "RH600": iR = (iSx+1)+(iSs*10)+1; break;
            case "RH650": iR = (iSx+1)+(iSs*11)+1; break;
            case "RH700": iR = (iSx+1)+(iSs*12)+1; break;
            case "RH750": iR = (iSx+1)+(iSs*13)+1; break;
            case "RH800": iR = (iSx+1)+(iSs*14)+1; break;
            case "RH850": iR = (iSx+1)+(iSs*15)+1; break;
            case "RH900": iR = (iSx+1)+(iSs*16)+1; break;
            case "RH950": iR = (iSx+1)+(iSs*17)+1; break;
            case "RH1000": iR = (iSx+1)+(iSs*18)+1; break;
            case "WU100": iR = (iSx+2)+(iSs*0); break;
            case "WU150": iR = (iSx+2)+(iSs*1); break;
            case "WU200": iR = (iSx+2)+(iSs*2); break;
            case "WU250": iR = (iSx+2)+(iSs*3); break;
            case "WU300": iR = (iSx+2)+(iSs*4); break;
            case "WU350": iR = (iSx+2)+(iSs*5); break;
            case "WU400": iR = (iSx+2)+(iSs*6); break;
            case "WU450": iR = (iSx+2)+(iSs*7); break;
            case "WU500": iR = (iSx+2)+(iSs*8)+1; break;
            case "WU550": iR = (iSx+2)+(iSs*9)+1; break;
            case "WU600": iR = (iSx+2)+(iSs*10)+1; break;
            case "WU650": iR = (iSx+2)+(iSs*11)+1; break;
            case "WU700": iR = (iSx+2)+(iSs*12)+1; break;
            case "WU750": iR = (iSx+2)+(iSs*13)+1; break;
            case "WU800": iR = (iSx+2)+(iSs*14)+1; break;
            case "WU850": iR = (iSx+2)+(iSs*15)+1; break;
            case "WU900": iR = (iSx+2)+(iSs*16)+1; break;
            case "WU950": iR = (iSx+2)+(iSs*17)+1; break;
            case "WU1000": iR = (iSx+2)+(iSs*18)+1; break;
            case "WU0": iR = 62; break;
            case "CAPE": iR = 63; break;
            case "CIN": iR = 64; break;
            case "PRATE": iR = 67; break;
            case "PWAT": iR = 65; break;
            case "LI": iR = 66; break;
            case "HGT500": iR = 25; break;                
        }
        return iR;
    }
    
    private int xRap(String paramIn) {
        int iR = 0;
        switch(paramIn.toUpperCase()) {
            case "CCL": iR = 238; break;
            case "FZLV": iR = 258; break;
            case "WZLV": iR = 224; break;
        }
        return iR;
    }
    
    public int xSrfX(String paramIn) {
        final int iSx = 10;
        final int iSs = 3;
        int iR = 0;
        switch(paramIn.toUpperCase()) {              
            case "TC100": iR = (iSx+0)+(iSs*0); break;
            case "TC150": iR = (iSx+0)+(iSs*1); break;
            case "TC200": iR = (iSx+0)+(iSs*2); break;
            case "TC250": iR = (iSx+0)+(iSs*3); break;
            case "TC300": iR = (iSx+0)+(iSs*4); break;
            case "TC350": iR = (iSx+0)+(iSs*5); break;
            case "TC400": iR = (iSx+0)+(iSs*6); break;
            case "TC450": iR = (iSx+0)+(iSs*7); break;
            case "TC500": iR = (iSx+0)+(iSs*8)+1; break;
            case "TC550": iR = (iSx+0)+(iSs*9)+1; break;
            case "TC600": iR = (iSx+0)+(iSs*10)+1; break;
            case "TC650": iR = (iSx+0)+(iSs*11)+1; break;
            case "TC700": iR = (iSx+0)+(iSs*12)+1; break;
            case "TC750": iR = (iSx+0)+(iSs*13)+1; break;
            case "TC800": iR = (iSx+0)+(iSs*14)+1; break;
            case "TC850": iR = (iSx+0)+(iSs*15)+1; break;
            case "TC900": iR = (iSx+0)+(iSs*16)+1; break;
            case "TC950": iR = (iSx+0)+(iSs*17)+1; break;
            case "TC1000": iR = (iSx+0)+(iSs*18)+1; break;
            case "TC0": iR = 1; break;
            case "RH100": iR = (iSx+1)+(iSs*0); break;
            case "RH150": iR = (iSx+1)+(iSs*1); break;
            case "RH200": iR = (iSx+1)+(iSs*2); break;
            case "RH250": iR = (iSx+1)+(iSs*3); break;
            case "RH300": iR = (iSx+1)+(iSs*4); break;
            case "RH350": iR = (iSx+1)+(iSs*5); break;
            case "RH400": iR = (iSx+1)+(iSs*6); break;
            case "RH450": iR = (iSx+1)+(iSs*7); break;
            case "RH500": iR = (iSx+1)+(iSs*8)+1; break;
            case "RH550": iR = (iSx+1)+(iSs*9)+1; break;
            case "RH600": iR = (iSx+1)+(iSs*10)+1; break;
            case "RH650": iR = (iSx+1)+(iSs*11)+1; break;
            case "RH700": iR = (iSx+1)+(iSs*12)+1; break;
            case "RH750": iR = (iSx+1)+(iSs*13)+1; break;
            case "RH800": iR = (iSx+1)+(iSs*14)+1; break;
            case "RH850": iR = (iSx+1)+(iSs*15)+1; break;
            case "RH900": iR = (iSx+1)+(iSs*16)+1; break;
            case "RH950": iR = (iSx+1)+(iSs*17)+1; break;
            case "RH1000": iR = (iSx+1)+(iSs*18)+1; break;
            case "RH0": iR = 2; break;
            case "WU100": iR = (iSx+2)+(iSs*0); break;
            case "WU150": iR = (iSx+2)+(iSs*1); break;
            case "WU200": iR = (iSx+2)+(iSs*2); break;
            case "WU250": iR = (iSx+2)+(iSs*3); break;
            case "WU300": iR = (iSx+2)+(iSs*4); break;
            case "WU350": iR = (iSx+2)+(iSs*5); break;
            case "WU400": iR = (iSx+2)+(iSs*6); break;
            case "WU450": iR = (iSx+2)+(iSs*7); break;
            case "WU500": iR = (iSx+2)+(iSs*8)+1; break;
            case "WU550": iR = (iSx+2)+(iSs*9)+1; break;
            case "WU600": iR = (iSx+2)+(iSs*10)+1; break;
            case "WU650": iR = (iSx+2)+(iSs*11)+1; break;
            case "WU700": iR = (iSx+2)+(iSs*12)+1; break;
            case "WU750": iR = (iSx+2)+(iSs*13)+1; break;
            case "WU800": iR = (iSx+2)+(iSs*14)+1; break;
            case "WU850": iR = (iSx+2)+(iSs*15)+1; break;
            case "WU900": iR = (iSx+2)+(iSs*16)+1; break;
            case "WU950": iR = (iSx+2)+(iSs*17)+1; break;
            case "WU1000": iR = (iSx+2)+(iSs*18)+1; break;
            case "WU0": iR = 3; break;
            case "CAPE": iR = 5; break;
            case "CIN": iR = 6; break;
            case "PRATE": iR = 68; break;
            case "PWAT": iR = 7; break;
            case "LI": iR = 9; break;
            case "HGT500": iR = 45; break;
        }
        return iR;
    }
	
    public double g2d(String lineIn, int gribSpot, boolean doOffset) { return parseGrib2Data(lineIn, gribSpot, doOffset); }
    public double g2dD(String lineIn, int gribSpot, boolean doOffset) { return parseGrib2DataD(lineIn, gribSpot, doOffset); }
	public ArrayList<Integer> getHeights(String detail) { return heights(detail); }
    public int cmc(String paramIn) { return xCmc(paramIn); }
    public int gfs(String paramIn) { return xGfs(paramIn); }
	public int hrrr(String paramIn) { return xHrrr(paramIn); }
    public int hrrrB(String paramIn) { return xHrrrB(paramIn); }
    public int hrwX(String paramIn) { return xHrwX(paramIn); }
    public int nam(String paramIn) { return xNam(paramIn); }
    public int rap(String paramIn) { return xRap(paramIn); }
    public int srfX(String paramIn) { return xSrfX(paramIn); }
        
}
