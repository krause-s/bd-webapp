$(document).ready(function($) {
	init();
});

/* initialization of DataTable for songs incl. DOM changes (layout) */
function init() {
	var table = "t";
//	var processing = "r";
	var length = "l";
	var info = "i";
	var pagination = "p";
	
	$('#songs').DataTable({
//		"processing": true,
		
		"dom": 
			getTag("panel-body", table /*+ processing*/) +			
			getTag("panel-footer",
				getTag("#paginationRow.row",
						getTag("col-lg-5 col-sm-4 hidden-xs", length) +
						getTag("#info-lg.col-lg-2 visible-lg", info) +
						getTag("#pagination.col-lg-5 col-sm-8", pagination)) +
				getTag("row hidden-lg",
						getTag("#info-non-lg", info) +
						getTag("visible-xs", length))),

		"lengthMenu": [
				[10, 25, 50, 100, -1], 
				[10, 25, 50, 100, "All"]],
				
		"order" : [ 
				[ 0, 'asc' ], 
				[ 1, 'asc' ], 
				[ 2, 'asc' ], 
				[ 3, 'asc' ]],
				
		"scrollX": true,
		searching : false/*,
		"serverSide": true*/
	});	
}

function getTag(clsId, content) {
	return "<'" + clsId + "'" + content + ">";
}