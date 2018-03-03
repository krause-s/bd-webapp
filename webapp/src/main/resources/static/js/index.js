$(document).ready(function($) {
	setHandler("show", "up");
	setHandler("hide", "down");
});

function setHandler(event, direction) {
	var arrow = 'glyphicon glyphicon-chevron-' + direction;
	
	$(".collapse").on(event + '.bs.collapse', function() {
		$("#stockCtrl").attr('class', arrow);
	});
}

