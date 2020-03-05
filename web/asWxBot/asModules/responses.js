/*
by Anthony Stump
Created: 4 Mar 2020
Updated: 5 Mar 2020
*/


const lol = require('./asModules/resp/lol.js');
const nearMe = require('./asModules/resp/nearMe.js');
const pho = require('./asModules/resp/pho.js');
const randomQuote = require('./asModules/resp/randomQuote.js');
const wxRadar = require('./asModules/respo/wxRadar.js');

module.exports = {

		lol : function(msg) { lol.getLaughing(msg) },
		nearMe : function(msg) { nearMe.getNearMe(msg) },
		pho : function(msg) { pho.getPho(msg) },
		randomQuotes : function(msg) { randomQuote.getRandomQuotes(msg) },
		wxRadar: function(msg, site) { wxRadar.getWeatherRadar(msg, site) }
		
}
