(function (angular) {
    'use strict';

    angular.module('authentication.directives')
        .directive('authentication', function () {
            return {
                restrict: 'E',
                controller: AuthenticationController,
                controllerAs: 'authenticationCtrl',
                templateUrl: 'authentication/authentication'
            };
        });

    AuthenticationController.$inject = ['authentication'];

    function AuthenticationController(authentication) {
        this.currentUser = 'anonymousUser';
        this.isAuthenticated = false;
        var self = this;
        this.getAuthentication = function () {
            authentication.getAuthentication().then(function (resp) {
                self.currentUser = resp.data.userName;
                self.isAuthenticated = resp.data.authenticated;
            });
        };
    }
}(window.angular));