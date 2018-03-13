var topBottomHeight;

$(document).ready(function($) {
	setDimensions();
	
	setCollapseHandler("show", "up");
	setCollapseHandler("hide", "down");
	
	$("footer a").click(function() {
		$("html, body").animate({
			scrollTop : 0
		}, "slow");
	});
	
	$('.bs-slider').slider({
//		tooltip: 'always',
//		tooltip_position:'bottom',
		range: true
	});
});

function setDimensions() {
	if (topBottomHeight == null)
		topBottomHeight = getTopBottomHeight();

	$("footer").height(topBottomHeight);

	var margin = topBottomHeight + "px 0px " + topBottomHeight + "px";
	//	console.log("margin: " + margin)
	$("body").css("margin", margin);
}

function getTopBottomHeight() {
	var navHeight = $("nav").height();
	return parseInt(navHeight);
}

function getInnerHeight() {
	var winHeight = $(window).height();
	
	if (topBottomHeight == null)
		topBottomHeight = getTopBottomHeight();

	return winHeight - (2 * topBottomHeight);
}

function setCollapseHandler(event, direction) {
	var arrow = 'glyphicon glyphicon-chevron-' + direction;

	$(".collapse").on(event + '.bs.collapse', function() {
		$("#collapsePanel").attr('class', arrow);
	});
}