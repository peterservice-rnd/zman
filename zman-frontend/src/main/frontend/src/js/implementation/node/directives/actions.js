(function (angular) {
    'use strict';
    angular.module('node.directives')
        .directive('actions', function () {
            return {
                restrict: 'E',
                templateUrl: 'main/node/actions'
            };
        });
}(window.angular));
