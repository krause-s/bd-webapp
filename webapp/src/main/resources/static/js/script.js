$(document).ready(function($){
	setDimensions();
});

$("footer a").click(function(){
	$("html, body").animate({ 
		scrollTop: 0 
	}, "slow");
});

function setDimensions() {
	var navHeight = $("nav").css("height");
	var top = parseInt(navHeight);
	var bottom = top + 10;
	
	var margin = top + "px 0px " + bottom + "px";
	console.log("margin: " + margin)
	
	$("footer").css("height", navHeight);
	$("body").css("margin", margin);
}

/*
var caret = '<span class="caret"></span>';

$('.collapse').on('show.bs.collapse', function () {
	dropCollapse('<span class="dropup">' + caret + '</span>');
});
$('.collapse').on('hide.bs.collapse', function () {
	dropCollapse(caret);
});

function dropCollapse(str) {
	$("#collapse").html(str);
}
*/