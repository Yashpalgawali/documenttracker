/**
 * 
 */
 
	$(document).ready(function(){

		var docarr = "";
		let appname = $('#appname').val();
		let baseurl = $('#base_url').val();
		
		var nyear,nmonth,ndate;
	
		$.ajax({
			
			type     : "GET",
		    url      : baseurl+"/viewalldocuments",
		    //url      : "viewalldocuments",
			dataType : "json",
			success  : function(result) 
			{	
				for(var i=0;i<result.length;i++)
				{
					const dt = new Date(result[i].last_renewed_date);
					const todaysdate = new Date();
					
					nyear 	=	dt.getFullYear(); //new Date(result[i].last_renewed_date).getFullYear();
					nmonth	=	dt.getMonth()+1;  //new Date(result[i].last_renewed_date).getMonth()+1;
					ndate	=	dt.getDate(); 	  //new Date(result[i].last_renewed_date).getDate();
					
					nmnt = ""+nmonth;
					
					if(nmnt.length==1){
						nmonth = '0'+nmonth;	
					}
					var nyeardate = (nyear+(result[i].license_duration))+"-"+nmonth+"-"+ndate;
					const diffTime = Math.abs(new Date(nyeardate).getTime() - new Date(todaysdate).getTime());
					var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
					
					if(diffDays >=1 && diffDays <=30){
						alert(result[i].doc_name+" needs to be renewed on or before "+nyeardate);
					}
					
					docarr = docarr 
							//+"<tr><td>" + (sr--)  
							//+"<tr><td><a href='viewfile?id="+result[i].doc_id+"'>"+result[i].doc_name+"</a>"
							+"<tr><td><a href='fileexists?id="+result[i].doc_id+"'>"+result[i].doc_name+"</a>"
							+"</td><td>"+result[i].email
							+"</td><td>"+result[i].issued_date
							+"</td><td>"+result[i].last_renewed_date
							+"</td><td id='nrenewdate'>"+(Number(nyear+result[i].license_duration))+"-"+nmonth+"-"+ndate
							+"</td><td><a href='getdocbyid?id="+result[i].doc_id+"'><i class='far fa-edit fa-lg'></i>&nbsp;&nbsp;Update</a>&nbsp;&nbsp;<a onclick='deldocbyid("+result[i].doc_id+")'  href><i class='far fa-trash fa-lg'></i>&nbsp;&nbsp;Delete</a></td></tr>";
				}
				
				$(docarr).appendTo("#docbody");
				$('#doctable').DataTable( {
			        "order": [ 5, "asc" ],
			        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
			        	
			        	var rowCount = $("#doctable tr").length;
						
			        	for(var i=0;i<rowCount;i++){
			        	
			        		var tdata = aData[4];
							const todaysdate = new Date();
							const diffTime = Math.abs(new Date(tdata).getTime() - new Date(todaysdate).getTime());
							const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 	
			        	
							if(diffDays >=1 && diffDays <=30)
					      	{
					    		$('td', nRow).css('color', 'Red');
					      	}
					    }	
					}
			    });
			},
			error: function(e){
				alert(JSON.stringify(e));
				console.log(JSON.stringify(e));
			}
		});
	});
	
	
	function deldocbyid(did)
	{
		let res = confirm("Are you sure?");
		if(res)
		{
			$.ajax({
					url      : baseurl+"/deldocbyid/"+did,
					//url 	: '/deldocbyid/'+did,
					type 	: 'GET',
					dataType: 'json',
					success : function(result)
					{
						if(result=="success")
						{
							alert("Document with Id "+did+" is deleted successfully");	
						}
						else{
							alert("Document with Id "+did+" is not deleted ");
						}
					},
					error : function(err)
					{
						alert(""+err);	
					}
			});
		}
		else{
			alert("Document is not deleted");
		}
	}