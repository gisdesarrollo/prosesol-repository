$(function(){
	
	var beneficio = [];
	
	$(".agregarBeneficio").click(function(){	
		cloneF();
	});
	
	
	function cloneF(){
		
		var $lbl = $(".lblBeneficio");
		
		$("#listaBeneficios:first")
		.clone()
		.appendTo(".cartWrapper");
		
	}
	
	$('.cartWrapper').on('change', '.changeFields', function(){
		
		var $set = $(this).closest('#listaBeneficios')
		
		var valBeneficio = $set.find(".beneficios :selected:last").val();

		beneficio.push(valBeneficio);
		
	});
	
	

});