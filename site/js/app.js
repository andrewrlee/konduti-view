'use strict';


angular.module('userStories', ['userStories.filters', 'userStories.services', 'userStories.directives', 'ngSanitize', 'ngResource']).
  config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {templateUrl: 'partials/home.html', controller: IndexCtrl});
	$routeProvider.when('/section/root > Notes', {redirectTo: '/notes/'});
    $routeProvider.when('/section/:currentSection', {templateUrl: 'partials/section.html', controller: SectionCtrl});
    $routeProvider.when('/section/:currentSection/story/:storyName', {templateUrl: 'partials/story.html', controller: StoryCtrl});
    $routeProvider.when('/notes/', {templateUrl: 'notes/index.html', controller: NotesCtrl});
    $routeProvider.otherwise({redirectTo: '/'});
  }]);

