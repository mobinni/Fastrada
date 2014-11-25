'use strict';

describe('HomeCtrl', function () {
    var scope, $httpBackend;//we'll use this scope in our tests

    //mock Application to allow us to inject our own dependencies
    beforeEach(angular.mock.module('fastradaApp'));
    //mock the controller for the same reason and include $rootScope and $controller
    beforeEach(angular.mock.inject(function ($rootScope, $controller, $injector) {
        //create an empty scope
        scope = $rootScope.$new();
        $httpBackend = $injector.get('$httpBackend');
        $httpBackend.whenGET('json/data.json').respond();
        //declare the controller and inject our empty scope
        $controller('HomeCtrl', {$scope: scope});
    }));

    // No logic to test
    it('should have variable text = "hello"', function () {
        expect(scope.test).toBe('hello');
    });

    it('should return aggregated packages', function () {
        var packets = [
            {time: 100, content: 20},
            {time: 105, content: 40},
            {time: 110, content: 30},
            {time: 115, content: 40},
            {time: 120, content: 40},
            {time: 125, content: 40},
            {time: 130, content: 40},
            {time: 140, content: 40},
            {time: 145, content: 40},
        ];
        scope.interval = 100;
        var aPackets = scope.aggregatePackets(packets);
        expect(aPackets[0].content).toBe(20);
        expect(aPackets.length).toBe(1);

    });





});