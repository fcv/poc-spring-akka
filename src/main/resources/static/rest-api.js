$(function($) {

	var $mainPainel = $('#main-content'),
		DEFAULT_AJAX_OPTIONS = {
			dataType: 'json',
			contentType: 'application/json'
		};

	function init() {

		$('form', $mainPainel).submit(function(e) {

			e.preventDefault();
			var $form = $(this);
			var action = $form.attr('action'),
				method = $form.attr('method') || 'get',
				data = $form.serialize();

			method = method.toUpperCase();
			

			var reqOpts = $.extend({}, DEFAULT_AJAX_OPTIONS, {
				url: action,
				data: data
			});

			$.ajax(reqOpts)
				.done(function(data, statusText, jqXHR) {
					
					$('.output-console', $mainPainel)
						.text(JSON.stringify(data, null, 2));
				});
		});
	};

	init();
})