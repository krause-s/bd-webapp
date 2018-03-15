var topBottomHeight;

$(document).ready(function($) {
	setDimensions();
	
	setCollapseHandler("show", "#collapsePanel", "up", "#yearPanel", "down");
	setCollapseHandler("hide", "#collapsePanel", "down", "#yearPanel", "up");
	
	$("footer a").click(scroll);
	
	/* initialization of bootstrap-slider */
	$('.bs-slider').slider({
//		tooltip: 'always',
		range: true
	});
});

/* set footer height in relation to navbar -> increase body-margin */
function setDimensions() {
	if (topBottomHeight == null)
		topBottomHeight = getTopBottomHeight();

	$("footer").height(topBottomHeight);

	var margin = topBottomHeight + "px 0px " + topBottomHeight + "px";
	$("body").css("margin", margin);
}
function getTopBottomHeight() {
	var navHeight = $("nav").height();
	return parseInt(navHeight);
}

/* content/client area height */
function getInnerHeight() {
	var winHeight = $(window).height();
	
	if (topBottomHeight == null)
		topBottomHeight = getTopBottomHeight();

	return winHeight - (2 * topBottomHeight);
}

/* dynamic collapse for glyphicon-chevron */
function setCollapseHandler(event, selector1, direction1, selector2, direction2) {
	if ($(selector1).size() != 0) {
		collapse(event, selector1, direction1);
	} else if ($(selector2).size() != 0) {
		collapse(event, selector2, direction2);
	}
}

function collapse(event, selector, direction) {
	var arrow = 'glyphicon glyphicon-chevron-' + direction;
	
	$(".collapse").on(event + '.bs.collapse', function() {
		$(selector).attr('class', arrow);
	});
}

/* scroll to top */
function scroll() {
	$("html, body").animate({
		scrollTop : 0
	}, "slow");
}