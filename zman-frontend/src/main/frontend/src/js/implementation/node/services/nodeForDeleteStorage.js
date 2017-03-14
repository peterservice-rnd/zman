(function (angular) {
    'use strict';

    angular.module('node.services')
        .service('nodeForDeleteStorage', NodeForDeleteStorage);

    NodeForDeleteStorage.$inject = [];
    function NodeForDeleteStorage() {
        var node;
        this.setNode = function (n) {
            node = n;
        };
        this.getNode = function () {
            if (node !== undefined) {
                return node;
            }
            return {};
        };
    }
}(window.angular));