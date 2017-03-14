(function (angular) {
    'use strict';
    angular.module('app.directives')
        .directive('modals', function () {
            return {
                restrict: 'E',
                templateUrl: 'main/modals'
            };
        });
}(window.angular));