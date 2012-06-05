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
jQuery(document).ready (function () {
	
	jQuery('#add-entity').click (function () {
		jQuery('#edit_image_form').hide ();
		jQuery('#add_edit_form table tbody tr td:last-child').each (function (index) {
			jQuery(this).children ().val ('');
			jQuery(this).children ('textarea').text ('');
		});	
		jQuery('#add-entity').hide ();
		jQuery('#add_edit_form').show ();
		return false;
	});
	
	jQuery('#cancel-edit').click (function () {
		jQuery('#add_edit_form').hide ();
		jQuery('#add-entity').show ();
		return false;
	});
	
	jQuery('#edit_image_form button').click (function () {
		jQuery('#edit_image_form').hide ();
	});	
	
	//setTimeout ("htmlentity ();", 1000);
	htmlentity ();
	
	jQuery('.select_group').change (function () {
		$(this).parent ().submit ();
	});
	
	jQuery('.datepicker').datepicker ();
	
});
function edit () {
	var global_arguments = arguments;
	jQuery('#add_edit_form table tbody tr td:last-child').each (function (index) {
		var str = global_arguments[index];
		str = str.split ("&#39"). join ("'");
		str = str.split ("<br />"). join ("\n");
		str = str.split ("&quot;"). join ("\"");
		jQuery(this).children ().val (str);
		jQuery(this).children ('textarea').text (str);
	});
	jQuery('#add-entity').hide ();
	jQuery('#add_edit_form').show ();
}
function edit_image (id) {
	jQuery('#edit_image_form .item_id').val (id);
	jQuery('#edit_image_form').show ();
	jQuery('#add_edit_form').hide ();
}
function htmlentity () {
	jQuery('table td.data').each (function () {
		//console.log ($(this).html ());
		jQuery(this).html (jQuery(this).html ().split ("&amp;#39"). join ("'"));
		jQuery(this).html (jQuery(this).html ().split ("&amp;quot;"). join ("\""));
		//console.log ($(this).html ().replace ("&amp;#39", "'"));
	});
}