(function (angular) {
    'use strict';

    angular.module('node.directives')
        .directive('childrenList', function () {
            return {
                restrict: 'E',
                controller: ChildNodesController,
                controllerAs: 'childCtrl',
                templateUrl: 'main/node/children'
            };
        });

    ChildNodesController.$inject = ['aceModelStorage', 'treeHandler', 'nodeForEditStorage', 'nodeForDeleteStorage', 'childStorage'];
    function ChildNodesController(modelStorage, treeHandler, editStorage, delStorage, childStorage) {

        this.getSelectedId = function () {
            return childStorage.getSelectedId();
        };

        this.getChildren = function () {
            if (treeHandler.isSet()) {
                return treeHandler.get().children;
            }
            return [];
        };

        this.selectChild = function (id, node) {
            childStorage.setSelectedId(id);
            editStorage.setNode(node);
            modelStorage.set('edit', node.znodeValue);
        };

        this.goToChild = function (node) {
            node.expand();
            node.select();
        };

        this.deleteChild = function (id, node) {
            delStorage.setNode(node);
            if (childStorage.getSelectedId() === id) {
                childStorage.setSelectedId(null);
                editStorage.setNode(treeHandler.get());
            }
        };
    }
}(window.angular));