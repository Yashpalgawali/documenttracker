/**
 * 
 */
 $(document).ready(function(){
		
		$('#issued_date').datetimepicker({
			
			format	:	'YYYY-MM-DD',
			icons: {
				time	: 'fa fa-clock-o',
				date	: 'fa fa-calendar',
				up		: 'fa fa-chevron-up',
				down	: 'fa fa-chevron-down',
				previous: 'fa fa-chevron-left',
				next	: 'fa fa-chevron-right',
				today	: 'fa fa-check',
				clear	: 'fa fa-trash',
				close	: 'fa fa-times'
			}
		});
		$('#last_renewed_date').datetimepicker({
			format	:	'YYYY-MM-DD',
			icons: {
				time	: 'fa fa-clock-o',
				date	: 'fa fa-calendar',
				up		: 'fa fa-chevron-up',
				down	: 'fa fa-chevron-down',
				previous: 'fa fa-chevron-left',
				next	: 'fa fa-chevron-right',
				today	: 'fa fa-check',
				clear	: 'fa fa-trash',
				close	: 'fa fa-times'
			}
		});
		
		
		
	});