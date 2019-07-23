$(document).ready(function(){
	
	$(document).delegate('.addRow', 'click', function(){
		
		var content = $('#tBeneficiosHidden tr'),
		size = $('#tBeneficios > tbody > tr').length + 1,
		element = null,
		element = content.clone();
		element.attr('id', "rec-"+size);
		element.find('.removeRow').attr('data-id', size);
		element.appendTo("#tbBeneficios");
		element.find('.sn').html(size);
		
	});
	
	$(document).delegate('.removeRow', 'click', function(){
		var id = $(this).attr("data-id");
		var targetDiv = $(this).attr('targetDiv');
		
		$('#rec-' + id).remove();
		
		$('#tbBeneficios tr').each(function(index){
			$(this).find('span.sn').html(index + 1);
		});
		
	});
	
});