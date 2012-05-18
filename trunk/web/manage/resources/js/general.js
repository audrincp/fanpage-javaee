$(document).ready (function () {
	
	$('#add-entity').click (function () {
		$('#add_edit_form table tbody tr td:last-child').each (function (index) {
			$(this).children ().val ('');
			$(this).children ().text ('');
		});	
		$('#add-entity').hide ();
		$('#add_edit_form').show ();
		return false;
	});
	
	$('#cancel-edit').click (function () {
		$('#add_edit_form').hide ();
		$('#add-entity').show ();
		return false;
	});
	
	//setTimeout ("htmlentity ();", 1000);
	htmlentity ();
	
});
function edit () {
	var global_arguments = arguments;
	$('#add_edit_form table tbody tr td:last-child').each (function (index) {
		var str = global_arguments[index];
		str = str.split ("&#39"). join ("'");
		str = str.split ("<br />"). join ("\n");
		str = str.split ("&quot;"). join ("\"");
		$(this).children ().val (str);
		$(this).children ().text (str);
	});
	$('#add-entity').hide ();
	$('#add_edit_form').show ();
}
function htmlentity () {
	$('table td.data').each (function () {
		//console.log ($(this).html ());
		$(this).html ($(this).html ().split ("&amp;#39"). join ("'"));
		$(this).html ($(this).html ().split ("&amp;quot;"). join ("\""));
		//console.log ($(this).html ().replace ("&amp;#39", "'"));
	});
}