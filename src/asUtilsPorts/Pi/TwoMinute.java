package asUtilsPorts.Pi;

import asUtilsPorts.SNMP.PiPusher;

public class TwoMinute {

	public static void main(String[] args) {

            PiPusher piPusher = new PiPusher();
            piPusher.snmpPi();

	}

}
