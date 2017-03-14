(function (angular) {
    'use strict';

    angular.module('alias.directives')
        .directive('mainMenu', function () {
            return {
                restrict: 'E',
                controller: CurrentAliasController,
                controllerAs: 'currentAliasCtrl',
                templateUrl: 'main/main-menu'
            };
        });

    CurrentAliasController.$inject = ['serverConnection', 'aliasStorage', 'url'];
    function CurrentAliasController(connection, storage, url) {
        var server = null;

        this.isSet = function () {
            return server !== null;
        };

        this.get = function () {
            server = connection.getAlias();
            if (!this.isSet()) {
                return 'No server';
            }
            return server.alias;
        };

        this.disconnect = function () {
            url.updateAlias('');
        };

        this.refresh = function () {
            storage.refresh();
        };
    }
}(window.angular));