$('.formInput').submit(function(e){

	var curpText = $('.curpText').after().next('.validation');	
	var rfcText = $('.rfcText').after().next('.validation');	
	var nssText = $('.nssText').after().next('.validation');
	
	var focusSet = false;
	var focusSetNss = false;
	var focusSetRfc = false;
	
	var curpLength = 18;
	var nssLength = 11;
	var rfcLength = 13;
		
	if(!$('.rfcText').val()){
		
		rfcText.remove();
		
		if($('.rfcText').after().next('.validation').length == 0){
			$('.rfcText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>Campo requerido</span>");
		}
		e.preventDefault();
		if(!focusSet){
			$('.rfcText').focus();
		}		
	}else if($('.rfcText').val()){
		
		rfcText.remove()
		var inputLength = $('.rfcText').val().length;
		
		if(inputLength < rfcLength){
			$('.rfcText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>El RFC no cumple con la longitud correcta</span>");
			e.preventDefault();
		}		
		if(!focusSet){
			$('.rfcText').focus();
		}		
	}
	
	if($('.nssText').length){
		if(!$('.nssText').val()){
			
			nssText.remove();
			
			if($('.nssText').after().next('.validation').length == 0){
				$('.nssText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>Campo requerido</span>");
			}
			e.preventDefault();
			if(!focusSet){
				$('.nssText').focus();
			}		
		}else if($('.nssText').val()){
			
			nssText.remove();
			var inputLength = $('.nssText').val().length;
			
			if(inputLength < nssLength){
				$('.nssText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>El NSS no cumple con la longitud correcta</span>");
				e.preventDefault();
			}else if(!$.isNumeric($('.nssText').val())){
				$('.nssText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>Proporcione solo números</span>");
				e.preventDefault();
			}		
			if(!focusSet){
				$('.nssText').focus();
			}		
		}
	}
	
	if($('.curpText').val().length > 0){
		
		curpText.remove();
		var inputLength = $('.curpText').val().length;		
		
		if(inputLength < curpLength){
			$('.curpText').after("<span class='validation' style='color:red;margin-bottom: 20px;'>El curp no cumple con la longitud correcta</span>");
			e.preventDefault();	
		}
		
		if(!focusSet){
			$('.rfcText').focus();
		}
		
	}else{
		return true;
	}
	
});

