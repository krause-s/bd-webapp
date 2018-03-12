$(document).ready(function($) {
	overlay("upload", "evaluation");	
	
	setCollapseHandler("show", "up");
	setCollapseHandler("hide", "down");

	setQuoteHandler();
	
	
	
	$('.checkTest').change(function() {
		var row = $(this).parent().parent();
		var name = row.find("input:first");
		name.attr("disabled", "disabled");
	});
});

function overlay(first, second) {
	$("#" + first + "Dialog").modal();	// TODO deprecated
	$("#" + second + "Dialog").modal();
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