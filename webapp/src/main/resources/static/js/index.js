$(document).ready(function($) {
	overlay(".modal");	
	
	setCollapseHandler("show", "up");
	setCollapseHandler("hide", "down");

	setQuoteHandler();
});

function overlay(selector) {
	$(selector).modal();
	
	$("input:file").change(function() {
		// TODO upload, evaluation...
		var fileName = $(this).val();
		alert(fileName);
		
		$(selector).modal("hide");
	});
}

function setCollapseHandler(event, direction) {
	var arrow = 'glyphicon glyphicon-chevron-' + direction;

	$(".collapse").on(event + '.bs.collapse', function() {
		$("#stockCtrl").attr('class', arrow);
	});
}

function setQuoteHandler() {
	$(window).on('resize', function() {
		var cls = "blockquote-reverse";

		if ($(this).width() < 992)
			$("#middleQuote").addClass(cls);
		else
			$("#middleQuote").removeClass(cls);
	});
	
	$(window).trigger('resize');
}