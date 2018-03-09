$(document).ready(function($) {	
	$('.sliderExample').slider({
		formatter: function(value) {
			return 'Current value: ' + value;
		}
	});
});