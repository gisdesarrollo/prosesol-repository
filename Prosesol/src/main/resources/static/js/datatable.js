$(document).ready(function(){	
	var table = $('#afiliados').DataTable({						
		'ajax' : '/data/afiliados',
		'processing' : true,
		'serverSide' : true,
		'paging' : true,
		columns : [{
			data : 'nombre'
		},{
			data : 'apellidoPaterno'
		},{
			data : 'apellidoMaterno'
		},{
			data : 'clave'
		},{
			data : 'saldoAcumulado',
			defaultContent : ""
		}
		],
		dataSrc : ""
	});
});