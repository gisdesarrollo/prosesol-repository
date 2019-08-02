// Script para Time Picker
var KTBootstrapTimepicker = function() {
    // Private functions
    var demos = function() {
        // minimum setup
        $('.hora').timepicker({
            minuteStep: 5,
            defaultTime: '12:00',
            showSeconds: false,
            showMeridian: true,
            snapToStep: true
        });
    }
    return {
        // public functions
        init: function() {
            demos();
        }
    };
}();
jQuery(document).ready(function() {
    KTBootstrapTimepicker.init();
});

// Script para Date Picker
$('.fecha').datepicker({
    format: "dd/mm/yyyy",
    todayBtn: "linked",
    language: "es",
    orientation: "botom auto",
    beforeShowYear: function(date){
          if (date.getFullYear() == 2007) {
            return false;
          }
        },
    toggleActive: true
});
