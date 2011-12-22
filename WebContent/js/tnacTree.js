var glengthRoot;
var gIndex=0;
var gArray;

function getPieData(listes,totalVote){
	var container = new Array();
	var p=0;
	var cumul=0;
	for (i in listes){
		var obj=listes[i];
		var newObj=new Array();
		newObj[0]=obj.name;
		newObj[1]=obj.vote;
		cumul+=obj.vote;
		container[i]=newObj;
		p=p+obj.pourcentage;
		if (obj.pourcentage<2 || (p>=90 && p<=99)){
			var lastObj=new Array();
			var r=listes.length-parseInt(i)-1;
			lastObj[0]="Autres ("+r+" listes)";
			lastObj[1]=totalVote-cumul;
			container[parseInt(i)+1]=lastObj;
			break;
		}
		
	}
	return container;
}


function buildRoot(){
	var appendElements = function() {
		var index=$("#tnacTree").children().length;
		var oldHtml= $("#tnacTree").html();
		var li=buildTreeItem(gArray[index].resource!=undefined,gArray[index]);
		$("#tnacTree").html(oldHtml+li);
		$("#progressbar").progressbar({ value: ((index+1)/glengthRoot)*100 });
		if (index==glengthRoot-1){
			   $("#progressbar").progressbar({ value: 97});
			   $("#tnacTree").treeview({
			    	collapsed: true,
			  });	
			   $("#progress").hide();
			   $("#progressbar").hide();
			   $("#tnacTree").show();	
			   $("a.res").click(function(event){
					 event.preventDefault();
					 loadResult($(this).attr("href"));
				 });
			   $("a.agg").click(function(event){
					 event.preventDefault();
					 loadAggResult($(this).attr("href"));
				 });

			   
		}
		else setTimeout(appendElements, 10);
	};
	setTimeout(appendElements, 10);
}

function loadAggResult(path){
	$.ajax({
		   url: 'agg/'+path,
		   type: 'get',
		   success: function(data) {
			   buildView(data);
		   }
		});
}

function loadResult(path){
	$.ajax({
		   url: 'couch'+"/"+"tnac/v1/bureau/"+path,
		   type: 'get',
		   success: function(data) {
			   buildView(data);
		   }
		});
}

function buildView(data){
	  if (data.error==0){
		  $("#mainView").html("<h1>NO DATA AVAILABLE FROM ISIE</h1>");
		  return;
	  }
	  $("#mainView").hide();
	  var table=buildOverview(data);
	  var container='<div id="container" class="highcharts-container" style="height: 800px; margin: 0 2em; clear: both; min-width: 1024px"></div>';

	  $("#mainView").html(table+container);
	  chart = new Highcharts.Chart({
	      chart: {
	         renderTo: 'container',
	         plotBackgroundColor: null,
	         plotBorderWidth: null,
	         plotShadow: false
	      },
	      title: {
	         text: 'Resultat par Bureau de vote'
	      },
	      tooltip: {
	         formatter: function() {
	            return '<b>'+ this.percentage.toFixed(2)  +'%' +'</b>:';
	         }
	      },
	      plotOptions: {
	         pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	               enabled: true,
	               color: Highcharts.theme.textColor || '#000000',
	               connectorColor: Highcharts.theme.textColor || '#000000',
	               formatter: function() {
	                  return '<b>'+ this.point.name +'</b>';
	               }
	            }
	         }
	      },
	       series: [{
	         type: 'pie',
	         name: 'Votes Par Liste',
	         data: getPieData(data.resultat.listes,data.resultat.bulletins.correct)
	      }]
	   });

	  window.resizeBy(1, 0); 
	  window.resizeBy(-1, 0); 
	  $("#mainView").show();

}

function buildOverview(data)
{
	var s="<table class='overview'><thead><tr><td>circonscription</td><td>delegation</td><td>centre_vote</td><td>bureau_vote</td><td>Registered</td><td>Voted</td></tr></thead>";
	s=s+"<tbody><tr><td>"+data.circonscription.name+"</td><td>"+data.delegation.name+"</td><td>"+data.centre_vote.name+"</td><td>"+data.bureau_vote.name;
	s=s+"</td><td>"+data.resultat.electeurs.enregistre+"</td><td>"+data.resultat.electeurs.votant+"</td></tr></tbody></table>";
	return s;
}

function buildSubtree(array){
	var html="";
	for (c in array){
		var li=buildTreeItem(array[c].resource!=undefined,array[c]);
		html=html+li;
	}
	return html;
	
}

function buildTreeItem(isFolder,c){
	var classItem=(isFolder)?"folder":"file";
	var result="<li><span res='"+c.resource+"' code='"+c.code+"' class='"+classItem+"'>"+link(c.name,isFolder,c.path)+"</span>";
	if (isFolder){
		result=result+"<ul>"+buildSubtree(c.children)+"</ul>";
	}
	return result+ "</li>";
}

function link (name,isFolder,path)
{
	if (isFolder)
		return '<a class="agg" href="'+path+'">'+name+'</a>';
	else{
		return '<a class="res" href="'+path+'">'+name+'</a>';
	}
	
}

$(document).ready(function(){

	$.ajax({
		   url: 'md',
		   type: 'get',
		   success: function(data) {
			   $("#progressbar").progressbar({ value: 1 });
			   $("#tnacTree").hide();
			   gArray=data.children;
			   glengthRoot=gArray.length;
			   buildRoot();
			}
		});

}); 