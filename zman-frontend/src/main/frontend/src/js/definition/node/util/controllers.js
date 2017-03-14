(function (angular, AceControllerFactory) {
    'use strict';

    var aceControllerFactory = new AceControllerFactory();

    angular.module('node.util.controllers', ['node.util.services'])
        .controller('AceCreateController', aceControllerFactory.create('create'));
}(window.angular, window.AceControllerFactory));