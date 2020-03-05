/*
by Anthony Stump
Created: 4 Mar 2020
Updated: on creation
*/


const lol = require('./asModules/resp/lol.js');
const pho = require('./asModules/resp/pho.js');

module.exports = {

		lol : function(msg) { lol.getLaughing(msg) },
		pho : function(msg) { pho.getPho(msg) }
		
}
