$(function($) {

	var ENDPOINT_URL = '/random-generator.ws',
		stompClient = null;

	function connect() {
		var socket = new SockJS(ENDPOINT_URL);
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {

			console.log("Connected");
			var destination = '/topic/random';
			stompClient.subscribe(destination, function(random) {

				console.log('subscribe(\'%s\') random: %s', destination, random);
				onMessageReceived(random.body);
			});
		});
	};

	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		console.log("Disconnected");
	};

	function cleanOutput() {

		var $table = $('table.output-table > tbody').empty();
	};

	function onMessageReceived(message) {

		var $tr = $('<tr />')
			.append($('<td />', {
				text: new Date().toISOString()
			}))
			.append($('<td />', {
				text: message
			}))
			;

		$('table.output-table > tbody')
			.prepend($tr);
	}

	function init() {

		$('.connect-trigger').click(connect);
		$('.disconnect-trigger').click(disconnect);
		$('.clean-trigger').click(cleanOutput);
	};

	init();
});
