jQuery(function($){
	$.datepicker.regional['ru'] = {
		/*closeText: 'Закрыть',
		prevText: '&#x3c;Пред',
		nextText: 'След&#x3e;',
		currentText: 'Сегодня',
		monthNames: ['Январь','Февраль','Март','Апрель','Май','Июнь', 'Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
		monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн','Июл','Авг','Сен','Окт','Ноя','Дек'],
		dayNames: ['воскресенье','понедельник','вторник','среда','четверг','пятница','суббота'],
		dayNamesShort: ['вск','пнд','втр','срд','чтв','птн','сбт'],
		dayNamesMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
		weekHeader: 'Нед',*/
		dateFormat: 'dd.mm.yy',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
	$.datepicker.setDefaults($.datepicker.regional['ru']);
});
$(document).ready (function () {
	
	$('#add-entity').click (function () {
		$('#add_edit_form table tbody tr td:last-child').each (function (index) {
			$(this).children ().val ('');
			$(this).children ('textarea').text ('');
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
	
	$('.select_group').change (function () {
		$(this).parent ().submit ();
	});
	
	$('.datepicker').datepicker ();
	
});
function edit () {
	var global_arguments = arguments;
	$('#add_edit_form table tbody tr td:last-child').each (function (index) {
		var str = global_arguments[index];
		str = str.split ("&#39"). join ("'");
		str = str.split ("<br />"). join ("\n");
		str = str.split ("&quot;"). join ("\"");
		$(this).children ().val (str);
		$(this).children ('textarea').text (str);
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