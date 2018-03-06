$(document).ready(function($) {
	$('#testTable').DataTable({
//		"processing": true,
		
		"dom": 
//			"<'panel panel-default'<'panel-body'tr>" +
			"<'panel panel-default'<'panel-body't>" +
			"<'panel-footer'<'row'" +
			"	<'col-sm-4'l>" +
			"	<'col-sm-4'i>" +
			"	<'col-sm-4'p>>>>",
		
		"scrollX": true,	
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
		"order" : [ [ 0, 'asc' ], [ 1, 'asc' ], [ 2, 'asc' ], [ 3, 'asc' ] ],
		searching : false/*,
		"serverSide": true*/
	});
});