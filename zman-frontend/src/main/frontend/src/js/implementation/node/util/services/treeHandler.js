(function (angular) {
    'use strict';

    angular.module('node.util.services')
        .service('treeHandler', TreeHandler);

    TreeHandler.$inject = ['aceModelStorage', 'nodeStorage', 'nodeFinder'];
    function TreeHandler(modelStorage, nodeStorage, finder) {
        var selectedNode = null, nullNode = { znodeName: null };

        this.get = function () {
            if (!this.isSet()) {
                return nullNode;
            }
            return selectedNode;
        };

        this.set = function (node) {
            modelStorage.set('edit', node.znodeValue);
            nodeStorage.setPath(node.znodeName);
            selectedNode = node;
        };

        this.isSet = function () {
            return angular.isObject(selectedNode);
        };

        this.drop = function () {
            modelStorage.drop('edit');
            selectedNode = null;
        };

        this.selectParent = function () {
            var parent = finder.locateParent(this.get().znodeName);
            parent.select();
        };
    }
}(window.angular));