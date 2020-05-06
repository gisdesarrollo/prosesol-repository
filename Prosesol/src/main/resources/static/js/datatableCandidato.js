$(function(){

	$('#candidatos').DataTable({
		'ajax' : '/data/candidatos',
		'processing' : true,
		'serverSide' : true,
		'paging' : true,
		columns : [{
            data : 'id',
            render : function(data, type, row, meta){

                return type === 'display' ?	'<div class="dropdown">'+
                   '<button class="btn btn-info btn-xs dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fas fa-stream"></i></button>'+
                   '<div class="dropdown-menu animated--fade-in" aria-labelledby="dropdownMenuButton" >'+
                   '<a class="dropdown-item" id="editar" >Editar</a>'+
                   '<a class="dropdown-item border-top" id="estatus">Activar</a>'+
                   '<a class="dropdown-item border-top text-danger" id="borrar">Borrar</a>'+
                   '</div></div>'	:data;
            }
		},{
			data : 'nombre'
		},{
		    data : 'apellidoPaterno'
		},{
		    data : 'apellidoMaterno'
		},{
			data : 'email'
		},{
			data : 'telefonoFijo'
		},{
		    data : 'telefonoMovil'
		},{
		    data : 'rfc'
		}],
		dataSrc : ""

	});



	$('#candidatos tbody').on('click', '#editar', function(e){
		var tr = $(this).closest("tr");
		var data = $('#candidatos').DataTable().row(tr).data();
		document.location.href = '/candidatos/editar/' + data.id;
	});

	$('#candidatos tbody').on('click', '#estatus', function(e){
        var tr = $(this).closest("tr");
        var data = $('#candidatos').DataTable().row(tr).data();
        document.location.href = '/candidatos/estatus/' + data.id;
    });


	$('#candidatos tbody').on('click', '#borrar', function(e){

		var confirma=confirm('Deseas Eliminar El Candidato');

		if(confirma!=false){
			var tr = $(this).closest("tr");
			var data = $('#candidatos').DataTable().row(tr).data();
			document.location.href = '/candidatos/eliminar/' + data.id;

		}else{

		}
	});
   
});
