$(document).ready(function(){
	
	var rowBeneficios = $("#rowBeneficios").html();
	
	$(".addRow").click(function(){
		
		$("#listaBeneficio").append(rowBeneficios);
		
	});
	
	$("#removeRow").click(function(){
		$("#listaBeneficios").remove(rowBeneficios);
	});
	
});