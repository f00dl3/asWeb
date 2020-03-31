/*
by Anthony Stump
Created: 4 Mar 2020
Updated: 31 Mar 2020
*/

const cams = require('./resp/cams.js');
const cf6 = require('./resp/cf6.js');
const help = require('./resp/help.js');
const lol = require('./resp/lol.js');
const nearMe = require('./resp/nearMe.js');
const pho = require('./resp/pho.js');
const randomQuote = require('./resp/randomQuote.js');
const serverInfo = require('./resp/serverInfo.js');
const stocks = require('./resp/stocks.js');
const wxData = require('./resp/wxData.js');
const wxForecast = require('./resp/wxForecast.js');
const wxLatest = require('./resp/wxLatest.js');
const wxRadar = require('./resp/wxRadar.js');
const wxSarcastic = require('./resp/wxSarcastic.js');
const wxStationSearch = require('./resp/wxStationSearch.js');

module.exports = {

		generateHelpMessage: function(msg) { help.helpMessage(msg) },
		getCf6Data: function(msg, month) { cf6.getCf6Data(msg, month) },
		getServerInfo: function(msg) { serverInfo.getServerInfo(msg) },
		getStocks: function(msg, ticker) { stocks.getStocks(msg, ticker) },
		getWeatherCameras: function(msg) { cams.getWeatherCameras(msg) },
		getWeatherCameraLoop: function(msg) { cams.getWeatherCameraLoop(msg) },
		getWeatherData: function(msg, station, date) { wxData.getWeatherData(msg, station, date) },
		getWeatherForecast: function(msg) { wxForecast.getWeatherForecast(msg) },
		getWeatherLatest: function(msg) { wxLatest.getWeatherLatest(msg) },
		getWeatherRadar: function(msg, site) { wxRadar.getWeatherRadar(msg, site) },
		getWeatherSarcastic: function(msg) { wxSarcastic.getWeatherSarcastic(msg) },
		getWeatherStations: function(msg, searchString) { wxStationSearch.getWeatherStations(msg, searchString) },
		lol : function(msg) { lol.getLaughing(msg) },
		nearMe : function(msg) { nearMe.getNearMe(msg) },
		pho : function(msg) { pho.getPho(msg) },
		randomQuotes : function(msg) { randomQuote.getRandomQuotes(msg) }
		
}
