(function (angular, setTimeout) {
    'use strict';

    angular.module('node.services')
        .service('nodeStorage', NodeStorage);

    NodeStorage.$inject = ['serverConnection', 'nodeUnitOfWork', '$q'];
    function NodeStorage(server, nodeUnitOfWork, $q) {
        var path = null, alias = null, nodes = [];
        this.setPath = function (p) {
            path = p;
        };
        this.getPath = function () {
            return path;
        };
        this.setAlias = function (a) {
            alias = a;
        };
        this.getAlias = function () {
            return alias;
        };
        this.setNodes = function (n) {
            nodes = n.map(function (node) {
                return nodeUnitOfWork.markDirty(node);
            });
        };
        this.getNodes = function () {
            return nodes;
        };
        this.loadChildren = function (node) {
            node.childrenPromise = $q(function(resolve, reject) {
                if (node.znodeName === '/' && nodeUnitOfWork.isNew(node)) {
                    nodeUnitOfWork.markClean(node);
                }
                if (nodeUnitOfWork.isDirty(node)) {
                    server.getConnection().get({path: node.znodeName}, function (response) {
                        node.children = response.children;
                        nodeUnitOfWork.markClean(node);
                    }, function() {
                        reject();
                    });
                } else {
                    setTimeout(function () {
                        resolve();
                    }, 100);
                }
                setTimeout(function () {
                    resolve();
                }, 100);
            });
            return node.childrenPromise;
        };
    }
}(window.angular, window.setTimeout));