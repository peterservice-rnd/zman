(function (angular) {
    'use strict';

    angular.module('authentication.services')
        .service('authentication', Authentication);

    Authentication.$inject = ['$resource', 'url', '$http'];

    function Authentication($resource, url, $http) {
        this.getAuthentication = function () {
            return $http({
                url: 'authentication',
                method: 'GET'
            });
        }
    }
}(window.angular));