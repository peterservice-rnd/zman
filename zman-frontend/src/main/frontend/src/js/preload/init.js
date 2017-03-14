(function ($) {
	'use strict';
	$(function() {
		$('body').tooltip({
			selector: '[data-toggle=tooltip]',
			container: 'body'
		});
		$('.alert .close').click(function (e) {
			e.preventDefault();
			$(this).parents('.alert').fadeOut(300);
		});
	});
}(window.$));