(function (angular) {
    'use strict';
    angular.module('node.directives')
        .directive('tree', function () {
            return {
                restrict: 'E',
                templateUrl: 'main/node/tree'
            };
        });
}(window.angular));