$(document).ready (function () {
	
	$('#add-entity').click (function () {
		$('#add_edit_form table tbody tr td:last-child').each (function (index) {
			$(this).children ().val ('');
			$(this).children ().text ('');
		});	
		$('#add_edit_form').show ();
		return false;
	});
	
	$('#cancel-edit').click (function () {
		$('#add_edit_form').hide ();
		return false;
	});
	
});
function edit () {
	var global_arguments = arguments;
	$('#add_edit_form table tbody tr td:last-child').each (function (index) {
		$(this).children ().val (global_arguments[index]);
		$(this).children ().text (global_arguments[index]);
	});
	$('#add_edit_form').show ();
}