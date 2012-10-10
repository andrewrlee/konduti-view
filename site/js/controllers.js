'use strict';

/* Controls the breadcrumb section - updates on 'changedBreadcrumb' */
function BreadcrumbCtrl($scope, locationService){
	$scope.breadcrumb = [ { "name" : "Home", "path" : "" } ];
	$scope.$on('changedBreadcrumb', function(){
		$scope.breadcrumb = locationService.breadcrumb;
	});
}
BreadcrumbCtrl.$inject = [ '$scope', 'locationService' ];
	

/* A controller for the dev mode button */
function ModeCtrl($scope, modeService) {
	$scope.devMode = false;
	$scope.toggle = modeService.toggleMode;
	$scope.$on('toggleDevmode', function(){
		$scope.devMode = modeService.devMode;
		$(".iframe").colorbox({iframe:true, width:"80%", height:"80%"});
	});
}
ModeCtrl.$inject = [ '$scope', 'modeService'];


/* Controls the left menu section */
function LeftMenuCtrl($scope, $http, sections){
	sections.get(function(data) {
		$scope.sections = data.sections.children;
	});
}
LeftMenuCtrl.$inject = [ '$scope', '$http', 'sections'];


/* A controller for the Home page */
function IndexCtrl($scope, $http, $routeParams, locationService, sections) {
	locationService.setLocation("root");
	sections.get(function(data) {
		$scope.sections = data.sections.children;
	});
}
IndexCtrl.$inject = [ '$scope', '$http', '$routeParams', 'locationService', 'sections' ];


/* A controller for the Notes page */
function NotesCtrl($scope, $http, $routeParams, locationService ) {
	locationService.setLocation("root > Notes");
}
NotesCtrl.$inject = [ '$scope', '$http', '$routeParams', 'locationService' ];


/* A controller for the Story Details page */
function StoryCtrl($scope, $http, $routeParams, locationService, modeService, storyService) {
	locationService.setLocation($routeParams.currentSection);
	$scope.devMode = modeService.devMode;
	$scope.$on('toggleDevmode', function(){
		$scope.devMode = modeService.devMode;
	});
	storyService.get($scope, $routeParams.currentSection, $routeParams.storyName);
	$scope.getBadgeType = function(step){
		 if(! $scope.devMode){
			 return "";
		 }
		 switch (step.result.style) {
		    case "resultSuccess":
		        return  '<span class="badge badge-success">&nbsp;</span>'
		    case "resultFailure":
		    	return  '<span class="badge badge-important">&nbsp;</span>'
		    case "resultCurrent":
		    	return  '<span class="badge">&nbsp;</span>'
		    case "resultNotPerformed":
		    	return  '<span class="badge">&nbsp;</span>'
		    case "resultPending":
		    	return  '<span class="badge badge-warning">&nbsp;</span>'
		}
	}
	
	$scope.getListClass = function(){
		return $scope.devMode ? 'unstyled' : '';  
	}
	$scope.showExpansionButton = function(step){
		return step.messages.length !== 0;  
	}
	
	 $scope.$on('$viewContentLoaded', function(){
		 $(".iframe").colorbox({iframe:true, width:"80%", height:"80%"});
	 });
	 
}
StoryCtrl.$inject = [ '$scope', '$http', '$routeParams', 'locationService', 'modeService', 'storyService'];



/* A controller for each Section page */
function SectionCtrl($scope, $http, $routeParams, locationService, sectionService) {
	locationService.setLocation($routeParams.currentSection);
	$scope.currentSection = $routeParams.currentSection;
	sectionService.get($scope);
}
SectionCtrl.$inject = [ '$scope', '$http', '$routeParams', 'locationService', 'sectionService' ];

