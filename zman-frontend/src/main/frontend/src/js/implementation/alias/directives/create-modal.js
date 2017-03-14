(function (angular) {
    'use strict';

    angular.module('alias.directives')
        .directive('createAliasModal', function () {
            return {
                restrict: 'E',
                controller: CreateController,
                controllerAs: 'createAliasCtrl',
                templateUrl: 'modal/alias/create'
            };
        });

    CreateController.$inject = ['addAliasModel', 'aliasStorage', 'toast'];
    function CreateController(addAliasModel, aliasStorage, toast) {
        this.value = function () {
            return addAliasModel.get();
        };

        this.save = function () {
            aliasStorage.add(this.value()).then(function () {
                addAliasModel.clear();
                toast.success('Alias successfully created');
            }).catch(function (response) {
                toast.error(response.data.body);
            });
            toast.loading('Creating alias...');
        };
    }
}(window.angular));