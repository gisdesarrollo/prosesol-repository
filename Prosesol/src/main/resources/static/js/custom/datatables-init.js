$(document).ready(function() {
	 var table = $('#dataTable').DataTable( {
	        lengthChange: false
	    } );
	 
	    table.buttons().container()
	        .appendTo( '#dataTable_wrapper .col-md-6:eq(0)' );
} );

//$('table').dataTable({
//    dom: 'Bfrtip',
//    buttons: ['excel']
//    "language": {
//        "search": "Buscar:",
//        "emptyTable": "No hay registros en la tabla",
//        "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
//        "infoEmpty": "Mostrando 0 registros",
//        "infoFiltered": "(De un total de _MAX_ registros)",
//        "thousands": ",",
//        "lengthMenu": "Mostrar _MENU_ registros",
//        paginate: {
//            first: 'Primera',
//            previous: '<',
//            next: '>',
//            last: 'Ultima'
//        }
//    }
//});	