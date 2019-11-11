(function (angular) {
    'use strict';

    angular.module('node.util.services')
        .service('nodeUtils', NodeUtils);

    NodeUtils.$inject = ['nodeStorage', 'nodeFinder', 'nodeUnitOfWork', 'treeHandler', '$q'];
    function NodeUtils(storage, finder, uow, treeHandler, $q) {

        this.softRefresh = function (node) {
            var parent = finder.locateParent(node.znodeName);
            uow.markDirty(parent);
            return storage.loadChildren(parent).then(function () {
                var grandParent = finder.locateParent(parent.znodeName);
                parent.hasChildren = !!parent.children.length;
            });
        };

        this.forceRefresh = function () {
            var root = finder.find('/');
            return $q(function(resolve, reject) {
                uow.markDirty(root);
                storage.loadChildren(root).then(function(greeting) {
                    resolve();
                }, function(reason) {
                    reject(reason);
                });
            });
        };

    }
}(window.angular));