(function (angular) {
    'use strict';
    angular.module('toast', ['ngToast'])
        .service('toast', ['ngToast', function (ngToast) {
            var self = this, wrap;

            wrap = function (msg) {
                var wrapped = angular.copy(self);
                wrapped.dismiss = ngToast.dismiss.bind(ngToast, [msg]);
                return wrapped;
            };

            this.success = function (message) {
                return wrap(ngToast.success({
                    content: '<i class=\'fa fa-check\'></i> ' + message,
                    timeout: 3000,
                    dismissOnTimeout: true,
                    dismissOnClick: true
                }));
            };
            this.error = function (message) {
                return wrap(ngToast.danger({
                    content: '<i class=\'fa fa-close\'></i> ' + message,
                    dismissOnClick: true
                }));
            };
            this.loading = function (message) {
                return wrap(ngToast.info({
                    content: '<i class=\'fa fa-cog fa-spin\'></i> ' + message
                }));
            };
            this.warning = function (message) {
                return wrap(ngToast.warning({
                    content: '<i class=\'fa fa-warning\'></i> ' + message,
                    timeout: 5000,
                    dismissOnTimeout: true,
                    dismissOnClick: true
                }));
            };
        }])
    ;
}(window.angular));