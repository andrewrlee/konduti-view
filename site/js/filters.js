'use strict';

/* Filters */
var filters = angular.module('userStories.filters', []);

filters.filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    }
  }]);


filters.filter('jbehaveVariable', function() {
    return function(text) {
	  var before = String(text).replace('｟', '<strong>');
	  var after= String(before).replace('｠', '</strong>');
      return after;
    }
});


filters.filter('storyLink', ['jiraIssueViewUrl', function(url) {
    return function(text) {
	  return url + text;
    }
}]);