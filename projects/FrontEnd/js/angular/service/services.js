'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('fastradaApp.services', [])
    /*
     Data fetcher service that retrieves data from external sources
     */
    .service('dataFetcher', ['$http', function ($http) {
        var url = 'http://teamb.feedient.com:8080/fastrada/api/race/';
        return {
            getRaces: function () {
                return $http.get(url).then(
                    function (response) {
                        // success handler
                        return response.data;
                    }, function (response) {
                        // error handler
                        alert("Something went wrong while receiving data! - " + response.status);
                        return null;
                    });

            },
            getRaceData: function (id) {
                return $http.get(url + id + '/data').then(
                    function (response) {
                        // success handler
                        return response.data;
                    }, function (response) {
                        // error handler
                        alert("Something went wrong while receiving data! - " + response.status);
                        return null;
                    });
            },
            getRaceSpeedData: function (id) {
                return $http.get(url + id + '/data/speed').then(
                    function (response) {
                        // success handler
                        return response.data;
                    }, function (response) {
                        // error handler
                        alert("Something went wrong while receiving data! - " + response.status);
                        return null;
                    });
            },
            getRaceRPMData: function (id) {
                return $http.get(url + id + '/data/rpm').then(
                    function (response) {
                        // success handler
                        return response.data;
                    }, function (response) {
                        // error handler
                        alert("Something went wrong while receiving data! - " + response.status);
                        return null;
                    });
            },
            getRaceTemperatureData: function (id) {
                return $http.get(url + id + '/data/temperature').then(
                    function (response) {
                        // success handler
                        return response.data;
                    }, function (response) {
                        // error handler
                        alert("Something went wrong while receiving data! - " + response.status);
                        return null;
                    });
            }
        };
    }])

    /*
     Query handeling service that sends out a broadcast message when a user searches for a certain race,
     when the broadcast is sent it invokes the method in the home controller that updates the screen
     */
    .service('queryHandler', ['$rootScope', function ($rootScope) {
        var currentRace = "";
        return {
            setCurrentRaceId: function (race) {
                currentRace = race;
                $rootScope.$broadcast("newRaceQuery");
            },
            getCurrentRaceId: function () {
                return currentRace;
            }
        }
    }]);