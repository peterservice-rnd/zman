(function (angular, AceControllerFactory) {
    'use strict';
    angular.module('node.util.directives')
        .directive('nodeEdit', function () {
            return {
                restrict: 'E',
                controller: new AceControllerFactory().create('edit'),
                controllerAs: 'editNodeCtrl',
                templateUrl: 'main/node/edit'
            };
        });
}(window.angular, window.AceControllerFactory));