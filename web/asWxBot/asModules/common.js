let module.exports.trimForDiscord2 = function(message) { 

	var maxMessageSize = 256;
	return message.substring(0, maxMessageSize);

}
