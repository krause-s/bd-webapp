$(document).ready(function($) {
	 $("#upload-file-input").on("change", uploadFile);
	overlay("evaluation");	


	setQuoteHandler();
	
	
	
	$('.checkTest').change(function() {
		var row = $(this).parent().parent();
		var name = row.find("input:first");
		name.attr("disabled", "disabled");
	});
});

function uploadFile() {
	  $.ajax({
	    url: "/uploadFile",
	    type: "POST",
	    data: new FormData($("#upload-file-form")[0]),
	    enctype: 'multipart/form-data',
	    processData: false,
	    contentType: false,
	    cache: false,
	    success: function () {
	    		window.location.replace("/");
	    },
	    error: function () {
	      // Handle upload error
	      // ...
	    }
	  });
	} // function uploadFile

function overlay(second) {
	$("#" + second + "Dialog").modal();
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