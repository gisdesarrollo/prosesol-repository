$(document).ready(function(){
	$('.btnEliminarBeneficios').on('click', function(e){
		if(!$('.cbBeneficio').is(':checked')){
			alert('Seleccione algún beneficio para eliminar');
			e.preventDefault();
		}else{
			var retVal = confirm('¿Está seguro que desea eliminar el(los) elemento(s)?');
			
			if(retVal == true){
				return true;
			}else{
				return false;
			}
		}
	});
});
	
