(function (angular) {
    'use strict';

    angular.module('user.directives')
        .directive('currentUser', function () {
            return {
                restrict: 'E',
                controller: CurrentUserController,
                controllerAs: 'currentUserCtrl',
                templateUrl: 'user/user'
            };
        });

    CurrentUserController.$inject = ['currentUser'];

    function CurrentUserController(currentUser) {
        this.currentUser = 'anonymousUser';
        var self = this;
        this.getCurrentUser = function () {
            currentUser.getUser().then(function (resp) {
                self.currentUser = resp.data;
            });
        }
    }
}(window.angular));