(function (angular) {
    'use strict';

    angular.module('node.directives')
        .directive('createNodeModal', function () {
            return {
                restrict: 'E',
                controller: CreateController,
                controllerAs: 'createCtrl',
                templateUrl: 'modal/node/create'
            };
        });

    CreateController.$inject = ['serverConnection', 'nodeStorage', 'aceModelStorage', 'nodeUtils', 'toast'];
    function CreateController (server, storage, modelStorage, nodeUtils, toast) {
        var self = this;

        this.getCreationString = function () {
            var path = storage.getPath();
            if(path !== undefined && path !== null) {
                return 'Create child for ' + path;
            }
        };

        this.create = function () {
            var
                connection = server.getConnection(),
                path = {path:storage.getPath() + '/'},
                body = {
                    znodeName: storage.getPath() + '/' + this.key,
                    znodeKey: this.key,
                    znodeValue: modelStorage.get('create'),
                    hasChildren: false,
                    children: []
                },
                callback = function (node, resp) {
                    if (resp.conflicts.length > 0) {
                        var message =  'Created with conflicts: <br>' + resp.conflicts.join('<br>');
                        toast.warning(message);
                        return;
                    }
                    nodeUtils.softRefresh(node);
                    modelStorage.drop('create');
                    self.key = null;
                    toast.success('Znode successfully created');
                };
            if (this.overwrite) {
                connection.saveOverwrite(path, body, callback.bind(null, body));
                toast.loading('Creating znode...');
                return;
            }
            connection.save(path, body, callback.bind(null, body));
            toast.loading('Creating znode...');
        };
    }

}(window.angular));