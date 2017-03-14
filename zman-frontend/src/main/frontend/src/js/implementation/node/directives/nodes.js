(function (angular) {
    'use strict';

    angular.module('node.directives')
        .directive('nodes', function () {
            return {
                restrict: 'E',
                controller: NodesController,
                controllerAs: 'nodeCtrl',
                templateUrl: 'main/nodes'
            };
        });

    NodesController.$inject = ['serverConnection', 'nodeStorage', 'aceModelStorage', 'treeHandler', 'nodeUtils', 'nodeForEditStorage', 'toast', 'nodeForDeleteStorage'];
    function NodesController(server, nodeStorage, modelStorage, treeHandler, nodeUtils, editStorage, toast, delStorage) {
        var
            cachedAlias = null,
            loadNodes
        ;

        this.isAliasSelected = function () {
            return cachedAlias !== null;
        };

        this.isNodeSelected = function () {
            return this.isAliasSelected() && treeHandler.isSet();
        };

        this.editNode = function () {
            editStorage.setNode(treeHandler.get());
        };

        this.delete = function () {
            delStorage.setNode(treeHandler.get());
        };

        this.save = function () {
            if(modelStorage.has('edit') && treeHandler.isSet()) {
                var node = editStorage.getNode();
                node.znodeValue = modelStorage.get('edit');
                server.getConnection().update({path: node.znodeName}, node, function() {
                    nodeUtils.softRefresh(node);
                    modelStorage.set('edit-modal', 'refresh');
                    toast.success('New value successfully saved');
                }, function (resp) {
                    toast.error(resp.data.body);
                });
                toast.loading('Changing znode value...');
            }
        };

        this.getNodeTitle = function () {
            if(treeHandler.isSet()) {
                return treeHandler.get().znodeName;
            }
        };

        this.getEditNodeTitle = function () {
            return editStorage.getNode().znodeName;
        };

        loadNodes = function () {
            toast.loading('Connecting...');
            var result = server.getConnection().get();
            return result.$promise.then(function () {
                nodeStorage.setNodes([result]);
                toast.success('Connected');
            }).catch(function () {
                toast.error('Could not connect to server...');
            });
        };

        this.getNodes = function (a) {
            if (arguments.length) { // Hack for angular
                nodeStorage.setNodes(a);
            }
            if (server.getAlias() !== cachedAlias) {
                if (server.getConnection() === null) {
                    nodeStorage.setNodes([]);
                    cachedAlias = null;
                    treeHandler.drop();
                } else {
                    loadNodes();
                    cachedAlias = server.getAlias();
                    nodeStorage.setAlias(cachedAlias.alias);
                }
            }
            return nodeStorage.getNodes();
        };

        this.forceRefresh = function () {
            toast.loading('Refreshing...');
            nodeUtils.forceRefresh().then(function () {
                toast.success('Refreshed');
            });
        };
    }
}(window.angular));