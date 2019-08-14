$(function(){

	$('#afiliados').DataTable({
		'ajax' : '/data/afiliados',
		'processing' : true,
		'serverSide' : true,
		'paging' : true,
		columns : [{
			data : 'id',
			render : function(data, type, row, meta){
				return type === 'display' ?
						'<button class="btn m-2 rounded-0 btn-sm btn-primary">Editar</button>':
						data;
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
	
	$('#afiliados tbody').on('click', 'button', function(e){
		var tr = $(this).closest("tr");
		var data = $('#afiliados').DataTable().row(tr).data();
		document.location.href = '/afiliados/editar/' + data.id;
	});
	
});