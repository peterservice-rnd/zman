(function (angular) {
    'use strict';

    angular.module('alias.services')
        .service('addAliasModel', AddAliasModel);

    AddAliasModel.$inject = [];
    function AddAliasModel() {
        var server;

        this.get = function () {
            return server;
        };

        this.set = function (s) {
            server = s;
        };

        this.clear = function () {
            server = {
                alias: '',
                connectionString: '',
                login: '',
                password: ''
            };
        };

        this.clear();
    }
}(window.angular));