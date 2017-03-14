(function (angular) {
    'use strict';

    angular.module('alias.directives')
        .directive('deleteAliasModal', function () {
            return {
                restrict: 'E',
                controller: DeleteController,
                controllerAs: 'deleteAliasCtrl',
                templateUrl: 'modal/alias/delete'
            };
        });

    DeleteController.$inject = ['aliasStorage', 'toast', 'serverConnection'];
    function DeleteController(aliasStorage, toast, connection) {
        this.getAlias = function () {
            return aliasStorage.getCurrentAlias().alias;
        };

        this.delete = function () {
            var deletedAlias = angular.copy(this.getAlias());
            aliasStorage.delete().then(function () {
                toast.success('Alias successfully deleted');
                if (angular.isObject(connection.getAlias()) && deletedAlias === connection.getAlias().alias) {
                    connection.setAlias(null);
                }
            }).catch(function (response) {
                toast.error(response.data.body);
            });
            toast.loading('Deleting alias...');
        };
    }
}(window.angular));