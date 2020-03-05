package asUtilsPorts.Pi3;

import asUtilsPorts.SNMP.Pi3Pusher;

public class TwoMinute {

	public static void main(String[] args) {

            Pi3Pusher pi3Pusher = new Pi3Pusher();
            pi3Pusher.snmpPi3();

	}

}
