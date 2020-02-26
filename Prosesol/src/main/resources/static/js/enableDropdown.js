$('#diaCorte').click(function() {
	
	if (this.checked) {
		$("#inputCorte").attr("disabled",false);
	}else{
		$("#inputCorte").attr("disabled",true);
	}
		

});

$('#isPlan').click(function(){
    if (this.checked) {
    		$("#semanalRadio").attr("disabled", false);
    		$("#mensualRadio").attr("disabled", false);
    		$("#anualRadio").attr("disabled", false);
    	}else{
    		$("#semanalRadio").attr("disabled", true);
    		$("#mensualRadio").attr("disabled", true);
    		$("#anualRadio").attr("disabled", true);
    	}
});