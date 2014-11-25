'use strict';

/* Controllers */

angular.module('fastradaApp.controllers').
    controller('NavigationCtrl', ['$scope', '$http', 'queryHandler', function ($scope, $http, queryHandler) {

        /*
            Data retrieval that is used for typeahead directive, returns a promise that the typeahead directive uses to
            autocomplete what you fill in
         */
        $scope.getRaceData = (function () {
            return $http.get('http://teamb.feedient.com:8080/fastrada/api/race').then(function(response){
                return response.data;
            });
        });

        /*
            Search method that is invoked when a user presses the search button
         */
        $scope.search = (function() {
            queryHandler.setCurrentRaceId($scope.query.id);
        });

    }]);