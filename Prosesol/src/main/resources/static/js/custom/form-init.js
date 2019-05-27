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
	
	$( "#fechaAfiliacion" ).datepicker({
        format : 'dd/mm/yyyy',
        changeMonth : true,
        changeYear : true,
        yearRange: '-100y:c+nn',
        maxDate: "today"
    });
	
	$( "#fechaInicioServicio" ).datepicker({
        format : 'dd/mm/yyyy',
        changeMonth : true,
        changeYear : true,
        yearRange: '-100y:c+nn',
        maxDate: "today"
    });
	
	$('.curpText').keypress(function(e){
		
		var max = 18;
		
		if(e.which < 0x20){
			return;
		}
		if(this.value.length == max){
			e.preventDefault();
		}else if(this.value.length > max){			
			this.value = this.value.substring(0, max);
		}
		
	});
		
	$('.nssText').keypress(function(e){
		
		var max = 11;
		
		if(e.which < 0x20){
			return;
		}
		if(this.value.length == max){
			e.preventDefault();
		}else if(this.value.length > max){
			this.value = this.value.substring(0, max);
		}
	});
	
	$('.rfcText').keypress(function(e){
		
		var max = 13;
		
		if(e.which < 0x20){
			return;
		}
		if(this.value.length == max){
			e.preventDefault();
		}else if(this.value.length > max){
			this.value = this.value.substring(0, max);
		}
		
	});
	
});
