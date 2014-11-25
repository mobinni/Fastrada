'use strict';

/* Controllers */

angular.module('fastradaApp.controllers').
    controller('HomeCtrl', ['$scope', 'dataFetcher', 'queryHandler', function ($scope, dataFetcher, queryHandler) {
        var id = queryHandler.getCurrentRaceId();
        if (id === "") {
            dataFetcher.getRaces().then(function (data) {
                id = data[0].id;
                /*
                 Function that refreshes the data on the screen based on the currently selected race
                 */
                $scope.interval = 1000;
                $scope.test = "hello";
                function updateScreen() {
                    // Build Speed chart on startup
                    /*
                     Data inside and http call cannot be deferred
                     url: https://groups.google.com/forum/#!topic/angular/Pf4yQ0Z9he8
                     */


                    // Methods that are invoked to build the charts when a user switches between tabs
                    $scope.openRPM = (function () {
                        dataFetcher.getRaceRPMData(id).then(function (data) {
                            buildRPMChart(data);
                        });
                    });

                    $scope.openSpeed = (function () {
                        dataFetcher.getRaceSpeedData(id).then(function (data) {
                            buildSpeedChart(data);
                        });
                    });

                    $scope.openTemperature = (function () {
                        dataFetcher.getRaceTemperatureData(id).then(function (data) {
                            buildTemperatureChart(data);
                        });
                    });


                    /* open initially */
                    $scope.openSpeed();
                    $scope.openRPM();
                    $scope.openTemperature();

                }

                updateScreen();

                /*
                 Scope will invoke the updateScreen method when a user query's for a new race
                 */
                $scope.$on('newRaceQuery', function () {
                    id = queryHandler.getCurrentRaceId();
                    updateScreen();
                });

                /*
                 Methods that build charts
                 */

                function buildSpeedChart(packets) {
                    var speedData = $scope.aggregatePackets(packets);
                    var series = [
                        {
                            argumentField: 'time',
                            valueField: 'content'
                        }
                    ];

                    $("#chartSpeed").dxChart({
                        dataSource: speedData,
                        series: series,
                        commonSeriesSettings: {
                            argumentField: 'time',
                            type: 'line'
                        },
                        argumentAxis: {
                            label: { format: 'longTime'}
                        },
                        legend: {
                            visible: false
                        },
                        tooltip: {
                            enabled: true
                        },
                        adjustOnZoom: true
                    });

                    $("#chartSpeedRangeSelector").dxRangeSelector({
                        size: {
                            height: 120
                        },
                        margin: {
                            left: 10
                        },
                        scale: {
                            divisionValue: 1,
                            minRange: 1
                        },
                        dataSource: speedData,
                        chart: {
                            series: series
                        },
                        behavior: {
                            callSelectedRangeChanged: "onMoving"
                        },
                        selectedRangeChanged: function (e) {
                            var zoomChart = $("#chartSpeed").dxChart('instance');
                            zoomChart.zoomArgument(e.startValue, e.endValue);
                        }
                    });
                }

                function buildRPMChart(packets) {
                    var rpmData = $scope.aggregatePackets(packets);
                    var series = [
                        {
                            argumentField: 'time',
                            valueField: 'content'
                        }
                    ];

                    $("#chartRPM").dxChart({
                        dataSource: rpmData,
                        series: series,
                        commonSeriesSettings: {
                            argumentField: 'time',
                            type: 'line'
                        },
                        legend: {
                            visible: false
                        },
                        argumentAxis: {
                            label: { format: 'longTime'}
                        },
                        tooltip: {
                            enabled: true
                        },
                        adjustOnZoom: true
                    });

                    $("#chartRPMRangeSelector").dxRangeSelector({
                        size: {
                            height: 120
                        },
                        margin: {
                            left: 10
                        },
                        scale: {
                            divisionValue: 1,
                            minRange: 1
                        },
                        dataSource: rpmData,
                        chart: {
                            series: series
                        },
                        behavior: {
                            callSelectedRangeChanged: "onMoving"
                        },
                        selectedRangeChanged: function (e) {
                            var zoomChart = $("#chartRPM").dxChart('instance');
                            zoomChart.zoomArgument(e.startValue, e.endValue);
                        }
                    });
                }

                function buildTemperatureChart(packets) {
                    var tempData = $scope.aggregatePackets(packets);
                    var series = [
                        {
                            argumentField: 'time',
                            valueField: 'content'
                        }
                    ];

                    $("#chartTemperature").dxChart({
                        dataSource: tempData,
                        series: series,
                        commonSeriesSettings: {
                            argumentField: 'time',
                            type: 'line'
                        },
                        legend: {
                            visible: false
                        },
                        tooltip: {
                            enabled: true
                        },
                        argumentAxis: {
                            label: { format: 'longTime'}
                        },
                        adjustOnZoom: true
                    });

                    $("#chartTemperatureRangeSelector").dxRangeSelector({
                        size: {
                            height: 120
                        },
                        margin: {
                            left: 10
                        },
                        scale: {
                            divisionValue: 1,
                            minRange: 1
                        },
                        dataSource: tempData,
                        chart: {
                            series: series
                        },
                        behavior: {
                            callSelectedRangeChanged: "onMoving"
                        },
                        selectedRangeChanged: function (e) {
                            var zoomChart = $("#chartTemperature").dxChart('instance');
                            zoomChart.zoomArgument(e.startValue, e.endValue);
                        }
                    });
                }


                $scope.aggregatePackets = (function (packets) {
                    packets.sort(function (a, b) {
                        return a.timestamp - b.timestamp;
                    });

                    var size = packets.length;
                    var endSize = (size > 0) ? size - 1 : 0;
                    var interval = $scope.interval;

                    var newPackets = [];

                    var startTime = packets[0].timestamp;
                    var newPacket = {
                        content: packets[0].content,
                        time: packets[0].timestamp
                    };
                    newPackets.push(newPacket);
                    var endTime = packets[endSize].timestamp;

                    for (var i = startTime; i < endTime; i += interval) {
                        var timestamps = 0;
                        var contents = 0;
                        var numberOfPackets = 0;
                        for (var j = 0; j < packets.length; j++) {
                            if (packets[j].timestamp < (i + interval) && packets[j].timestamp > i) {
                                timestamps += packets[j].timestamp;
                                contents += packets[j].content;
                                numberOfPackets++;
                            }
                        }
                        var averageTime = Math.round((timestamps / numberOfPackets));
                        var averageContent = Math.round(contents / numberOfPackets);

                        newPacket = {
                            content: averageContent,
                            time: averageTime
                        };
                        newPackets.push(newPacket);
                    }

                    return newPackets;
                });


                $scope.$watch('interval', function () {
                    if ($scope.interval > 0) {
                        $scope.openSpeed();
                        $scope.openRPM();
                        $scope.openTemperature();
                    }
                });

            });
        }
    }]);