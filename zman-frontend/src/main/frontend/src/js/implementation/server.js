(function (angular) {
    'use strict';
    angular.module('server', ['ngResource', 'util.services'])
        .service('serverConnection', ServerConnection);

    angular.module('server')
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push(function(){
                return {
                    response: function(response) {
                        return response;
                    },
                    responseError: function(rejection) {
                        if (rejection.status === 401) {
                            alert("Session expired! Return on login form!");
                            window.location.reload();
                            throw new Error(rejection.statusText);
                        }
                        if (rejection.status === 500) {
                            alert("Error!");
                            throw new Error(rejection.statusText);
                        }
                        return rejection;
                    }
                }
            });
        }]);

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
