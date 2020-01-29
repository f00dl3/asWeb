/*
 * by Anthony Stump
 * Created December 2019
 * Updated 29 Jan 2020
 */

package asUtilsPorts.Pi2;

import asUtilsPorts.SNMP.Pi2Pusher;

public class TwoMinute {

	public static void main(String[] args) {

		Pi2Pusher snmp_pi2 = new Pi2Pusher();
		snmp_pi2.snmpPi2();

	}

}
