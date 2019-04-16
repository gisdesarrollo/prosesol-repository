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
	
	
    
});