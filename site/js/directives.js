'use strict';

/* Directives */

var module = angular.module('userStories.directives', []);

module.directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }]);

module.directive('externalLink', ['defaultExternalUrlPrefix', function(url) {
	return function(scope, elm, attrs) {
		var link = scope.link 
		if(link.substring(0, 5) != "http:"){
			 link = url +  link
		}
		elm.html("<a target=\"_blank\" href="  + link + ">" + scope.name+ "</a>");
	};
}]);