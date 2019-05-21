$('.selectValues').change(function(){
		var currentVal = tinyMCE.get('texteditor').getContent();	
		tinyMCE.get('texteditor').setContent(currentVal + ' ' + $(this).val());
	});