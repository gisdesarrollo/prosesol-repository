$(function() {
        $( "#datepicker" ).datepicker({
            dateFormat : 'dd-mm-yy',
            changeMonth : true,
            changeYear : true,
            yearRange: '-100y:c+nn',
            maxDate: "today"
        });
    });

$(function() {
    $( "#datepicker1" ).datepicker({
        dateFormat : 'dd-mm-yy',
        changeMonth : true,
        changeYear : true,
        yearRange: '-100y:c+nn',
        maxDate: "today"
    });
});