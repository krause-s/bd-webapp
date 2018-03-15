$(document).ready(function($) {
	$("#upload-file-input").on("change", uploadFile);
	$("#evaluationDialog").modal();

	setQuoteHandler();

	$('.placeCheck').change(disable);
});

/* upload of XLSX-file */
function uploadFile() {
	$("#idleDialog").modal();

	$.ajax({
		url : "/uploadFile",
		type : "POST",
		data : new FormData($("#upload-file-form")[0]),
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false,
		cache : false,
		success : function() {
			window.location.replace("/");
		}//,error : function() {}
	});
}

/* responsive alignment for 2nd quote */
function setQuoteHandler() {
	$(window).on('resize', quoteAlign);
	$(window).trigger('resize');
}
function quoteAlign() {
	var cls = "blockquote-reverse";

	if ($(this).width() < 992)
		$("#middleQuote").addClass(cls);
	else
		$("#middleQuote").removeClass(cls);
}

/* disabling/enabling of input in evaluation */
function disable() {
	var row = $(this).parent().parent();
	var name = row.find("input:first");

	if ($(this).is(':checked')) {
		name.removeAttr("disabled");
	} else {
		name.attr("disabled", "disabled");
	}
}