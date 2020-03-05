/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: on creation
 */

module.exports = {

	conv2Tf: function(tC) {
		var tF = (tC * 9/5) + 32;
		return tF.toFixed(0);
	}

}
