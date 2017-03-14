(function (angular) {
    'use strict';

    angular.module('node.services')
        .service('childStorage', ChildStorage);

    ChildStorage.$inject = [];
    function ChildStorage () {
        var selectedId;

        this.getSelectedId = function () {
            return selectedId;
        };

        this.setSelectedId = function (id) {
            selectedId = id;
        };

        this.drop = function () {
            selectedId = null;
        };
    }
}(window.angular));