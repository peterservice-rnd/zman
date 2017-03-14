(function (angular) {
    'use strict';
    angular.module('server', ['ngResource', 'util.services'])
        .service('serverConnection', ServerConnection);

    ServerConnection.$inject = ['$resource', 'url'];
    function ServerConnection($resource, url) {
        var alias = null;

        this.setAlias = function (serverAlias) {
            alias = serverAlias;
        };

        this.getConnection = function () {
            if (!angular.isObject(alias)) {
                return null;
            }
            return $resource(url.create('api/zk/' + alias.alias + ':path'), null, {
                update: { method: 'PUT' },
                saveOverwrite: { method: 'POST', params: {overwrite: 'true'}},
                deleteRecursive: { method: 'DELETE'}
            });
        };

        this.getAlias = function () {
            return alias;
        };
    }
}(window.angular));
