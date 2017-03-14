(function (angular) {
    'use strict';

    angular.module('alias.services')
        .factory('servers', Servers);

    Servers.$inject = ['$resource', 'url'];
    function Servers($resource, url) {
        return $resource(url.create('api/servers/:alias'), null, {
            update: { method: 'PUT' }
        });
    }
}(window.angular));