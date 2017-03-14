(function (angular) {
    'use strict';
    /**
     * Hack providing ability to easily manage angular modules
     */
    var modules = [], originalModule = angular.module;
    angular.module = function (moduleName) {
        if (modules[moduleName] !== undefined) {
            return modules[moduleName];
        }
        modules[moduleName] = originalModule.apply(angular, arguments);
        return angular.module.apply(angular, arguments);
    };
}(window.angular));
