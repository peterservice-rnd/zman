(function () {
    'use strict';

    angular.module('alias.directives')
        .directive('manageAliasModal', function () {
            return {
                restrict: 'E',
                controller: AliasController,
                controllerAs: 'aliasCtrl',
                templateUrl: 'modal/alias/manage'
            };
        });

    AliasController.$inject = ['aliasStorage', 'toast', 'url'];
    function AliasController(storage, toast, url) {
        var self = this;

        this.connect = function () {
            var alias = storage.getCurrentAlias();
            url.updateAlias(alias.alias);
        };

        /**
         * Setter and getter for selected alias (multi-select-related hack)
         * @param {Array} [currentAlias] Optional, don't pass if you want to get alias
         * @returns {Array}
         */
        this.selectedAliases = function (currentAlias) {
            if (arguments.length) {
                storage.setSelectedAliases([currentAlias[0]]);
            } else {
                return storage.getSelectedAliases();
            }
        };

        this.aliases = function () {
            return storage.aliases();
        };

        this.isSelected = function () {
            return storage.getSelectedAliases()[0] !== undefined;
        };

        this.getCurrentAlias = function () {
            return storage.getCurrentAlias();
        };

        this.refresh = function () {
            storage.refresh();
        };

        this.update = function () {
            storage.update().then(function (selected) {
                toast.success('Alias successfully updated ');
                self.selectedAliases(selected);
            }).catch(function (response) {
                toast.error(response.data.body);
            });
            toast.loading('Updating alias...');
        };
    }
}());