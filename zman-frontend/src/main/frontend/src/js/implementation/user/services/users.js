(function (angular) {
    'use strict';

    angular.module('user.services')
        .service('currentUser', CurrentUser);

    CurrentUser.$inject = ['$resource', 'url', '$http'];

    function CurrentUser($resource, url, $http) {
        this.getUser = function () {
            return $http({
                url: 'username',
                method: 'GET',
                transformResponse: function (resp) {
                    return resp;
                }
            });
        }
    }
}(window.angular));