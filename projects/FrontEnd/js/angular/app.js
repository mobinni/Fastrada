'use strict';


// Declare app level module which depends on filters, and services
angular.module('fastradaApp', [
        'ngRoute',
        'fastradaApp.services',
        'fastradaApp.controllers',
        'ngAnimate',
        'ui.bootstrap'
    ]).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeCtrl'});
        $routeProvider.otherwise({redirectTo: '/home'});
    }]);

angular.module('fastradaApp.services', []);
angular.module('fastradaApp.controllers', []);
