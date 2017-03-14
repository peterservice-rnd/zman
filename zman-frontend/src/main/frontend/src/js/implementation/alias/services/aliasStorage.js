(function (angular) {
    'use strict';

    angular.module('alias.services')
        .service('aliasStorage', AliasStorage);

    AliasStorage.$inject = ['servers', 'serverConnection', 'url', 'toast'];
    function AliasStorage(servers, connection, url, toast) {
        var aliases = [],
            selectedAliases = [],
            alias = null,
            originalAlias = null,
            self = this,
            find,
            serverFromUrl,
            updateAliases
        ;

        find = function (aliasName) {
            return aliases.filter(function (alias) {
                return alias.alias === aliasName;
            })[0];
        };

        updateAliases = function () {
            originalAlias = alias = null;
            aliases = servers.query();
            return aliases.$promise;
        };

        serverFromUrl = function () {
            var alias = url.getAlias();
            if (angular.isString(alias)) {
                var found = find(alias);
                if (found !== undefined) {
                    connection.setAlias(found);
                    self.setCurrentAlias(found);
                } else {
                    toast.error('Alias \'' + alias + '\' not found!');
                }
            } else {
                connection.setAlias(null);
            }
        };

        this.startup = function () {
            updateAliases().then(serverFromUrl);
        };

        this.startup();

        this.refresh = function () {
            if (angular.isObject(alias)) {
                var selected = alias.alias;
            }
            updateAliases().then(function () {
                var filtered = aliases.filter(function (element) {
                    return element.alias === selected;
                });
                self.setSelectedAliases(filtered);
            });
        };

        this.aliases = function () {
            return aliases.sort(function (one, another) {
                return one.alias.localeCompare(another.alias);
            });
        };

        this.getSelectedAliases = function () {
            return selectedAliases;
        };

        this.setSelectedAliases = function (aliases) {
            selectedAliases = aliases;
            this.setCurrentAlias(selectedAliases[0]);
        };

        this.getCurrentAlias = function () {
            if (alias !== null && alias !== undefined) {
                return alias;
            }
            return {};
        };

        this.setCurrentAlias = function (a) {
            originalAlias = a;
            alias = angular.copy(a);
        };

        this.delete = function () {
            return servers['delete']({alias: originalAlias.alias}).$promise.then(updateAliases);
        };

        this.update = function () {
            var selected = alias.alias;
            return servers.update({alias: originalAlias.alias}, alias).$promise
                .then(updateAliases)
                .then(function () {
                    return aliases.filter(function (element) {
                        return element.alias === selected;
                    });
                });
        };

        this.add = function (server) {
            return servers.save({}, server).$promise.then(updateAliases);
        };
    }
}(window.angular));