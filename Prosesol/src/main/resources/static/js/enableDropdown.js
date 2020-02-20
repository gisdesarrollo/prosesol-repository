$('#fecha').click(function() {
	
	if (this.checked) {
		$("#inputCorte").attr("disabled",false);
	}else{
		$("#inputCorte").attr("disabled",true);
	}
		

});