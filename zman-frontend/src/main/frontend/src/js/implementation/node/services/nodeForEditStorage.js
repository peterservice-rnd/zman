(function (angular) {
    'use strict';

    angular.module('node.services')
        .service('nodeForEditStorage', NodeForEditStorage);

    NodeForEditStorage.$inject = [];
    function NodeForEditStorage() {
        var node = null, originalNode = null;

        this.getPath = function () {
            if (node === null) {
                return '';
            }
            return node.znodeName;
        };

        this.getKey = function () {
            if (node === null) {
                return '';
            }
            return node.znodeKey;
        };

        this.setKey = function (key) {
            if (node === null) {
                return;
            }
            key = key.replace(/\//, '');
            node.znodeKey = key;
            node.znodeName = node.znodeName.split('/').drop(1).join('/') + '/' + key;
        };

        this.getOriginalPath = function () {
            if (originalNode === null) {
                return '';
            }
            return originalNode.znodeName;
        };

        this.setNode = function (n) {
            node = angular.copy(n);
            originalNode = n;
        };

        this.getNode = function () {
            if (node === null) {
                return {};
            }
            return node;
        };

        this.drop = function () {
            node = null;
        };
    }
}(window.angular));