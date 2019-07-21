$(document).ready(function(){
	
	var rowBeneficios = $("#rowBeneficios").html();
	var i = $('#listaBeneficio').length + 1;
	
	$(".addRow").on('click', function(){
		
		$(rowBeneficios).appendTo("#listaBeneficio");
		i++;
		
	});
	
	$("#listaBeneficio").on('click', '.removeRow', function(){
		if(i > 2){
			$(this).parents('#rowBeneficios').remove();
			i--;
		}
	});
	
});