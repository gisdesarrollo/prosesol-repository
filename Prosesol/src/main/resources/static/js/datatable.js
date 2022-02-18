
	
$(function(){
	var url;
	 var AActivos = $("#activa").val();
	 var ATotales = $("#totales").val();
	 var AVencidos = $("#vencidos").val();
	 if(AActivos!=undefined){ 
		 url='/data/activos';
	 }else if(ATotales!=undefined){
		 url='/data/afiliados'
	 }else if(AVencidos!=undefined){
		 url = '/data/vencidos'
	 }
		

	$('#afiliados').DataTable({
		'ajax' : url,
		'processing' : true,
		'serverSide' : true,
		'scrollY': true,
        'scrollX': true,
        'scrollCollapse': true,
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
		    data : 'rfc'
		},{
			data : 'saldoAcumulado'
		},{
			data : 'fechaAlta'
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

	$('#dataTablePagos').DataTable({
		"autoWidth": false,
          "stateSave": false,
		"paging": true,
		dom: "Bftip",
		
		 buttons: [{
                    extend: "excel",
                    className: "btn btn-sm",
                    text: '<i class="fa fa-file-excel"></i> Excel'
                },
                {
                    extend: "print",
                    className: "btn btn-sm",
                    text: '<i class="fa fa-print"></i> Imprimir'
                }
            ],
	"language": {
                "search": "Buscar:",
                "emptyTable": "No hay registros en la tabla",
                "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                "infoEmpty": "Mostrando 0 registros",
                "infoFiltered": "(De un total de _MAX_ registros)",
                "thousands": ",",
                "lengthMenu": "Mostrar _MENU_ registros",
                paginate: {
                    first: 'Primera',
                    previous: '<',
                    next: '>',
                    last: 'Ultima'
                }
            }
		});
});
