(function (angular) {
    'use strict';
    angular.module('node.util.directives')
        .directive('treeNode', ['RecursionHelper', function (RecursionHelper) {
            return {
                restrict: 'E',
                controller: ZnodeTreeController,
                controllerAs: 'treeCtrl',
                //scope: {
                //    'node': '='
                //},
                compile: function (element) {
                    return RecursionHelper.compile(element);
                },
                templateUrl: 'main/node/tree-node'
            };
        }]);

    ZnodeTreeController.$inject = ['treeHandler', 'nodeStorage', 'nodeForEditStorage', '$scope', 'url', '$rootScope', 'childStorage'];
    function ZnodeTreeController(treeHandler, nodeStorage, editStorage, $scope, url, $rootScope, childStorage) {
        var self = this, node = $scope.node, load;

        load = function () {
            self.loading = true;
            return nodeStorage.loadChildren(node).then(function () {
                $scope.$applyAsync(function () {
                    self.loading = false;
                });
            });
        };

        this.isSelected = function () {
            return treeHandler.get().znodeName === node.znodeName;
        };

        this.toggle = function () {
            return load().then($scope.toggle);
        };

        this.expand = function () {
            return load().then($scope.expand);
        };

        this.select = function () {
            childStorage.drop();
            treeHandler.set(node);
            editStorage.setNode(node);
            nodeStorage.loadChildren(node);
            url.updatePath(node.znodeName);
        };

        node.toggle = this.toggle.bind(this);
        node.select = this.select.bind(this);
        node.expand = this.expand.bind(this);
        $rootScope.$emit('nodeLoaded', node.znodeName);
    }
}(window.angular));