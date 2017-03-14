(function (angular) {
    'use strict';

    angular.module('node.directives')
        .directive('deleteNodeModal', function () {
            return {
                restrict: 'E',
                controller: DeleteController,
                controllerAs: 'deleteCtrl',
                templateUrl: 'modal/node/delete'
            };
        });

    DeleteController.$inject = ['nodeForDeleteStorage', 'toast', 'treeHandler', 'nodeUtils', 'serverConnection'];
    function DeleteController(delStorage, toast, treeHandler, nodeUtils, server) {
        this.getNodeName = function () {
            return delStorage.getNode().znodeName;
        };

        this.hasChildren = function () {
            return delStorage.getNode().hasChildren;
        };

        this.delete = function () {
            var nodeForDeletion = delStorage.getNode();
            server.getConnection().deleteRecursive({ path: nodeForDeletion.znodeName }, function() {
                if(nodeForDeletion.znodeName === treeHandler.get().znodeName) {
                    treeHandler.selectParent();
                }
                nodeUtils.softRefresh(nodeForDeletion);
                toast.success('Znode successfully deleted');
            }, function (resp) {
                toast.error(resp.data.body);
            });
            toast.loading('Deleting znode...');
        };
    }
}(window.angular));