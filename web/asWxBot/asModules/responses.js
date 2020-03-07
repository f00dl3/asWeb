/*
by Anthony Stump
Created: 4 Mar 2020
Updated: 6 Mar 2020
*/


const lol = require('./resp/lol.js');
const nearMe = require('./resp/nearMe.js');
const pho = require('./resp/pho.js');
const randomQuote = require('./resp/randomQuote.js');
const wxRadar = require('./resp/wxRadar.js');

module.exports = {

		lol : function(msg) { lol.getLaughing(msg) },
		nearMe : function(msg) { nearMe.getNearMe(msg) },
		pho : function(msg) { pho.getPho(msg) },
		randomQuotes : function(msg) { randomQuote.getRandomQuotes(msg) },
		wxRadar: function(msg, site) { wxRadar.getWeatherRadar(msg, site) }
		
}
