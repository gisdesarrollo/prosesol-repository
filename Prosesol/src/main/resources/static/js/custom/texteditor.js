$('#selectAfiliado').change(function(){
	var currentVal = tinyMCE.get('texteditor').getContent();
	tinyMCE.get('texteditor').setContent(currentVal + ' ' + "[[${afiliado." + $(this).val() + "}]]");
});

$('#selectServicio').change(function(){
	var currentVal = tinyMCE.get('texteditor').getContent();
	tinyMCE.get('texteditor').setContent(currentVal + ' ' + "[[${afiliado.servicio." + $(this).val() + "}]]");
});

$('#selectPromotor').change(function(){
	var currentVal = tinyMCE.get('texteditor').getContent();
	tinyMCE.get('texteditor').setContent(currentVal + ' ' + "[[${afiliado.promotor." + $(this).val() + "}]]");
});

$('#selectCuenta').change(function(){
	var currentVal = tinyMCE.get('texteditor').getContent();
	tinyMCE.get('texteditor').setContent(currentVal + ' ' + "[[${afiliado.cuenta."+ $(this).val() + "}]]");
});

$('#selectPeriodo').change(function(){
	var currentVal = tinyMCE.get('texteditor').getContent();
	tinyMCE.get('texteditor').setContent(currentVal + ' ' + "[[${afiliado.periodicidad." + $(this).val() + "}]]");
});