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

    $('#busqueda').click(function(e){

        var validacion = false;

        var nombreBusqueda = $('#nombreBusqueda').val();
        var apellidoPaternoBusqueda = $('#apellidoPaternoBusqueda').val();
        var apellidoMaternoBusqueda = $('#apellidoMaternoBusqueda').val();
        var rfcBusqueda = $('#rfcBusqueda').val();
        var telefonoBusqueda = $('#telefonoBusqueda').val();
        var claveBusqueda = $('#claveBusqueda').val();

        if(nombreBusqueda != "" || apellidoPaternoBusqueda != "" || apellidoMaternoBusqueda != ""
            || rfcBusqueda != "" || telefonoBusqueda != "" || claveBusqueda != ""){
            validacion = true;
        }else{
            validacion = false;
        }

        if(!validacion){
            alert("Por favor, ingrese un parámetro de búsqueda");
            e.preventDefault();
        }
    });

    $("#submitCV").click(function(e){
        var cuenta = $("#cuentasVigor").val();
        if(cuenta == 0){
            alert("Proporcione una cuenta");
            e.preventDefault();
        }
    });

});
	
