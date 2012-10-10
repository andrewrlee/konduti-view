'use strict';

/* Services Module */
var services = angular.module('userStories.services', []);


services.value('version', version);


services.value('reportLocation', dataLocation);


services.value('defaultExternalUrlPrefix', defaultExternalUrlPrefix);


services.factory('locationService', function($rootScope) {
	var locationService = {};
	locationService.breadcrumb = {}

	locationService.setLocation = function(location) {
		var result = [ {
			"name" : "Home",
			"path" : ""
		} ];
		var elements = location.split(">");
		var path = elements[0].replace(/^\s+|\s+$/g, '');
		for ( var i = 1; i < elements.length; i++) {
			var name = elements[i].replace(/^\s+|\s+$/g, '');
			path += " > " + name;
			result.push({
				"name" : name,
				"path" : "section/" + path
			});
		}
		this.breadcrumb = result;
		$rootScope.$broadcast('changedBreadcrumb')
	}
	return locationService;
});


services.factory('modeService', function($rootScope) {
	var modeService = {};
	modeService.devMode = false;
	modeService.toggleMode = function() {
		modeService.devMode = !modeService.devMode;
		$rootScope.$broadcast('toggleDevmode')
	}
	return modeService;
});


services.factory('storyService', ['$resource', 'sections', 'reportLocation', function($resource, sections, reportLocation) {
	var storyService = {};
	storyService.get = function($scope, currentSection, currentStoryName) {
		sections.get(function(data) {
			var pathWithOutRoot = currentSection.substr(7);
			var storyUrl = reportLocation + 'data/root.' + data.pathTodotSeperated[pathWithOutRoot] + '/' + currentStoryName + '.json';
			$resource(storyUrl).get(function(data) {
				$scope.story = data.story;
			}
		)});
	}
	return storyService;
}]);


services.factory('sectionService',['$resource', 'sections', function($resource, sections) {
   var sectionService = {};
   sectionService.get = function($scope) {
 	 sections.get(function(data) {
		var sections = data.sections.children;
		$scope.stories = [];
		$scope.children = [];
		for ( var section in sections) {
  		  var matchingPath = find($scope.currentSection, sections[section]);
		  if (matchingPath) {
			$scope.children = matchingPath.children;
			break;
		  }
		}
		//remove the original "root" section and then get stories based on path 
		var pathWithOutRoot = $scope.currentSection.substr(7);
		var dotSeperated = data.pathTodotSeperated[pathWithOutRoot];
		$scope.stories = data.sectionsToStories[dotSeperated];
	  })};

	  function find(path, element) {
		  if (!element) {return null;}
		  if (path == element.path){ return element; }
		  for ( var child in element.children) {
			var result = find(path, element.children[child]);
			if (result) { return result;}
		  }
		  return null;
	  }

	return sectionService;
} ]);


services.factory('sections', [ '$resource', 'reportLocation', function($resource, reportLocation) {
	return $resource(reportLocation + 'data/index.json');
} ]);
