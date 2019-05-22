$( document ).ready(function() {
	$('body').addClass("app header-fixed sidebar-fixed aside-menu-fixed sidebar-lg-show");
	
	$('table').addClass("table table-condensed table-responsive table-hover");
	
	$('head').append('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">');
	
	$("#inputNumeroInfonavit").prop("disabled", true);
	$("#infonavitCB").click(function(){
		if($(this).prop("checked") == true){
			$("#inputNumeroInfonavit").prop("disabled", false);
		}else if ($(this).prop("checked") == false){
			$("#inputNumeroInfonavit").prop("disabled", true);
		}
	});
	
	$("#get-data-form").submit(function(e){
		
		var content = tinymce.get("texteditor").getContent();
		$("#data-container").html(content);
		
		return false;
		
	});
	
	$("#cuentas").prop("disabled", false);
	$("#promotores").change(function(){
		var selectValue = $(this).children("option:selected").val();
		if(selectValue >= 1){
			$("#cuentas").prop("disabled", true);
			$("#cuentas option[value=0]").attr('selected', 'selected');
		}else{
			$("#cuentas").prop("disabled", false);			
			$("#cuentas").val(1);
		}
	});
	
	$( "#fechaNacimiento" ).datepicker({
        format : 'dd/mm/yyyy',
        changeMonth : true,
        changeYear : true,
        yearRange: '-100y:c+nn',
        maxDate: "today"
    });
	
	$('#curpText').on('keydown keyup change', function(){
		
		var valueCurp = $(this).val();
		var valueCurpLn = $(this).val().length;
		
		if(valueCurpLn < 18){
			$('#smallCurp').text('El campo no cuenta con la longitud correcta);
		}else{
			$('small').text('Longitud correcta');
		}
		
	});
	
});
