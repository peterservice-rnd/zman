(function (angular) {
    'use strict';
    angular.module('app.directives')
        .directive('app', function () {
            return {
                restrict: 'E',
                templateUrl: 'app'
            };
        });
}(window.angular));
