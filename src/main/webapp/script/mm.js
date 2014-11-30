var app = angular.module('movieApp', [], ['$httpProvider', function ($httpProvider) {
    //$httpProvider.defaults.headers.common['X-Parse-Application-Id'] = 'CdtHCpaXixz7bwudAlWvvO12HiMaWqGOUbjDPPBC';
   /// $httpProvider.defaults.headers.common['X-Parse-REST-API-Key'] = 'IxMctPQYe9WCvRiyFL6aXEDpBpBfbLCx3Dh2oo4j';
	
	 $httpProvider.responseInterceptors.push('myHttpInterceptor');
	 var spinnerFunction = function spinnerFunction(data, headersGetter) {
		    $("#spinner").show();
		 	//console.log("show");
		    return data;
		  };

		  $httpProvider.defaults.transformRequest.push(spinnerFunction);
}]);

app.controller('MovieController', ['$scope', '$http','$filter', function ($scope, $http, $filter) {
	
	 $scope.readonly = true;
	 $scope.orderByField = 'myMovie.imdbMovie.imdbRating';
	 $scope.reverseSort = true;
	  
	 $selectioncount= 0;
	 
    var displayTable = function () {
        $http.get('rest/mmapi/load').success(function (data) {
        	if(data.status=='success') {
        		$scope.records = data;
        		 $scope.properties = data.properties;
        		
        	} else {
        		glitterAlert("Warning Message!" , "Unable to load movies from database"+ data.error);
        	}
           
        });
    }
    displayTable();
    
   
    
    $scope.refresh = function () {
    	 displayTable();
    	 $scope.readonly = true;
    };
    
    $scope.scan = function () {
      $http.get('rest/mmapi/scan?location='+$scope.scanlocation).success(function (data) {
          $scope.records = data;
          $scope.readonly = false;
          if(data.records.length==0){
        	  glitterAlert("Warning Message !!" , "No movie files found in the location <b><i>" + $scope.scanlocation +"</i></b>. Please try againg with different location");
          }
      });
    };
    
    $scope.select = function (record, index) {
    
     $('.tr_selected').removeClass('tr_selected');
     var trIndex = index +2;
      $('#movietable tr:nth-child('+trIndex+')').addClass('tr_selected');	
      $scope.selectedIndex = $scope.records.records.indexOf(record);
      if($scope.records.isDBData){
    	  $scope.loadSimilar();
      }
    };
    
    $scope.lookup = function (name) {
            $http.get('rest/mmapi/lookup?movieName=' + name).success(function (data) {
            	
            	if(data.status=='success') {
            		$scope.records.records[ $scope.selectedIndex]["myMovie"]["imdbMovie"] =  data.records[0]["imdbMovie"];
            		$scope.records.records[ $scope.selectedIndex]["myMovie"].name = data.records[0]["imdbMovie"].Title; 
            	} else {
            		glitterAlert("Warning Message!" , "No movie found associated with the title <b><i>" + name + "</i></b>. Please make sure the title is correct! Error Message "+ data.error);
            	}
               
            });
       
    };
    $scope.update = function (mode) {
    	$http.post('rest/mmapi/update/'+ mode,$scope.records.records[ $scope.selectedIndex]).success(function (data) {
    		if(data.status=='success'){
    			if(mode == 'new') {
    				$scope.records.records.splice($scope.selectedIndex, 1);
    			}
    			glitterAlert("Information!!" , "Movie updated successfully");
    		} else {
        		glitterAlert("Error Message!" , "Could not update to database ! Error Message "+ data.error);
        	}
         });
    	
    };
    
    $scope.edit = function () {
    	 $scope.readonly = false;
    };
    
   
    
    $scope.delete = function (record, index) {
        if (confirm("Sure to Delete?")) {
        	$http.delete('rest/mmapi/delete/'+$scope.records.records[ $scope.selectedIndex].myMovie.name).success(function (data) {
        		if(data.status=='success'){        			
        				$scope.records.records.splice($scope.selectedIndex, 1);        			
        				glitterAlert("Information!!" , "Movie updated successfully");
        		} else {
            		glitterAlert("Error Message!" , "Could not update to database ! Error Message "+ data.error);
            	}
             });
        }
    };
    
    $scope.ignore = function (record, index) {         			
        	$scope.records.records.splice($scope.selectedIndex, 1);              
    };
    
    $scope.loadSimilar = function () {
    	if($scope.properties['rt_api_key']==''){
    		return;
    	}
    	var imdbid  = $scope.records.records[ $scope.selectedIndex].myMovie.imdbMovie.imdbID;
    	imdbid  = imdbid.substring(2,imdbid.length);
    	$http.jsonp("http://api.rottentomatoes.com/api/public/v1.0/movie_alias.json?apikey="+$scope.properties['rt_api_key'] +"&type=imdb&id="+imdbid+"&callback=JSON_CALLBACK").success(function(data){
    		if(angular.isDefined(data.links)) {
            $http.jsonp(data.links.similar+"?apikey=nnwwsqebr3fb4egs5yh6652d&callback=JSON_CALLBACK").success(function(data){
                $scope.similar_movies = data.movies;
                
            });
            }
        });
    };
    
    $scope.saveSettings = function () {
    	$http.post('rest/mmapi/savesetting/',$scope.properties).success(function (data) {
    		if(data.status=='success'){
    			$("#myModal").modal('hide');
    			glitterAlert("Information !" , "Settings updated sucessfully");
    		} else {
        		glitterAlert("Error Message!" , "Could not update to database ! Error Message "+ data.error);
        	}
         });
    };
    
    
    $scope.importData = function () {
    	 $("#spinner").show();
    	 $("#importModel").modal('hide');
    	var f = document.getElementById('file').files[0],
        r = new FileReader();
    	r.onloadend = function(e){
    		$scope.records = JSON.parse(e.target.result);
    		 $("#spinner").hide();
    		// console.log($scope.records.status);
     }
     r.readAsBinaryString(f);

    };
   
}]);

app.factory('myHttpInterceptor', function ($q, $window) {
	  return function (promise) {
	    return promise.then(function (response) {
	      $("#spinner").hide();
	    	//console.log("hide");
	      return response;
	    }, function (response) {
	      $("#spinner").hide();
	    	///console.log("hide");
	      return $q.reject(response);
	    });
	  };
	});


function glitterAlert(title, message){
	$.gritter.add({
		// (string | mandatory) the heading of the notification
		title: title,
		// (string | mandatory) the text inside the notification
		text: message
		//sticky: false

		
	});

	return false;



}
/*var config = {
    layout: {
        name: 'layout',
        padding: 4,
        panels: [
            { type: 'top',  size: 30, resizable: false, minSize:30 },
            { type: 'left', size: '30%', resizable: true, minSize: 300 },
            { type: 'main', minSize: 300 }
        ]
    },
    grid: { 
        name: 'grid',
        //url:  'rest/mmapi/loaddata',
        show: {
            toolbar          : true,
            toolbarDelete    : true
        },
        columns: [
            { field: 'movieName', caption: 'Movie Name', size: '33%', sortable: true, searchable: true },
            { field: 'genere', caption: 'Genere', size: '90px', sortable: true, searchable: true },
            { field: 'genere', caption: 'IMDB', size: '50px'},
            { field: 'rtrating', caption: 'RT', size: '50px'},
            { field: 'selfRating', caption: 'My Rating', size: '70px'},

            
        ],
      
        onClick: function(event) {
            var grid = this;
            var form = w2ui.form;
            console.log(event);
            event.onComplete = function () {
                var sel = grid.getSelection();
                console.log(sel);
                if (sel.length == 1) {
                    form.recid  = sel[0];
                    form.record = $.extend(true, {}, grid.get(sel[0]));
                    form.refresh();
                } else {
                    form.clear();
                }
            }
        }
    },
   
  
    toolbar :{
    	name: 'toolbar',
    	items: [
    		{ type: 'check',  id: 'item1', caption: 'Check', img: 'icon-page', checked: true },
    		{ type: 'break',  id: 'break0' },
    		{ type: 'menu',   id: 'item2', caption: 'Drop Down', img: 'icon-folder', items: [
    			{ text: 'Item 1', icon: 'icon-page' }, 
    			{ text: 'Item 2', icon: 'icon-page' }, 
    			{ text: 'Item 3', value: 'Item Three', icon: 'icon-page' }
    		]},
    		{ type: 'break', id: 'break1' },
    		{ type: 'radio',  id: 'item3',  group: '1', caption: 'Radio 1', icon: 'fa-star', checked: true },
    		{ type: 'radio',  id: 'item4',  group: '1', caption: 'Radio 2', icon: 'fa-star-empty' },
    		{ type: 'spacer' },
    		{ type: 'button',  id: 'item5',  caption: 'Item 5', icon: 'fa-home' }
    	]
    }
}

$(function () {
    // initialization
	
	
    $('#main').w2layout(config.layout);
    w2ui.layout.content('top', $().w2toolbar(config.toolbar));
    w2ui.layout.content('left', $().w2grid(config.grid));
    //w2ui['grid'].load("rest/mmapi/loaddata");
   // w2ui.layout.content('main',$("#form").html());
    
  
    
});*/