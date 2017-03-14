(function (angular) {
    'use strict';

    angular.module('node.util.services')
        .service('aceModelStorage', AceModelStorage);

    AceModelStorage.$inject = [];
    function AceModelStorage() {
        var storage = {};
        this.set = function (name, data) {
            storage[name] = data;
        };
        this.has = function (name) {
            return storage.hasOwnProperty(name);
        };
        this.get = function (name) {
            if (!this.has(name) || typeof storage[name] !== 'string') {
                return '';
            }
            return storage[name];
        };
        this.drop = function (name) {
            storage[name] = undefined;
        };
    }
}(window.angular));
