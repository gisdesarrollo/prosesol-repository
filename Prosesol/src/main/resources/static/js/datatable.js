$(function(){

	$('#afiliados').DataTable({
		'ajax' : '/data/afiliados',
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
                   '<a class="dropdown-item border-top" id="detalle">Detalle</a>'+
                   '<a class="dropdown-item border-top text-warning" id="cambiar">Activar o Desactivar Afiliado</a>' +
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
			data : 'clave'
		},{
			data : 'saldoAcumulado'
		},{
		    data : "isBeneficiario"
		},{
		    data: 'estatus'
		},{
		    data : 'servicio.nombre'
		}],
		dataSrc : ""

	});



	$('#afiliados tbody').on('click', '#editar', function(e){
		var tr = $(this).closest("tr");
		var data = $('#afiliados').DataTable().row(tr).data();
		document.location.href = '/afiliados/editar/' + data.id;
	});

	$('#afiliados tbody').on('click', '#detalle', function(e){
        var tr = $(this).closest("tr");
        var data = $('#afiliados').DataTable().row(tr).data();
        document.location.href = '/afiliados/detalle/' + data.id;
    });

    $('#afiliados tbody').on('click', '#beneficiario', function(e){
            var tr = $(this).closest("tr");
            var data = $('#afiliados').DataTable().row(tr).data();
            document.location.href = '/beneficiarios/crear/' + data.id;
        });

	$('#afiliados tbody').on('click', '#borrar', function(e){

		var mensaje='Deseas Eliminar Este Afiliado';
		var confirma=confirm('Deseas Eliminar El Afiliado');

		if(confirma!=false){
			var tr = $(this).closest("tr");
			var data = $('#afiliados').DataTable().row(tr).data();
			document.location.href = '/afiliados/eliminar/' + data.id;

		}else{

		}
	});

	$('#afiliados tbody').on('click', '#cambiar', function(e){
        var tr = $(this).closest("tr");
        var data = $('#afiliados').DataTable().row(tr).data();
        document.location.href = '/afiliados/cambiar_estatus/' + data.id;
    });

    $('#dataTable').DataTable();
});
