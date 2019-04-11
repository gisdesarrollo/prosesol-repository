// Call the dataTables jQuery plugin

$(document).ready( function () {
    $('#myTable').DataTable("autoWidth": false,
            "stateSave": false,
            "paging": true,
            "order": [columna, orden],
            // responsive: responsivo,
            dom: descarga,
            scrollX: varscrollX,
            scrollY: varscrollY,
            orderCellsTop: orderCellsTop,
            fixedHeader: fixedHeader,
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
                });
} );