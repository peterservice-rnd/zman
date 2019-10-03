(function (angular) {
    'use strict';
    angular.module('app', ['ngAnimate', 'templates', 'aliases', 'nodes', 'authentications', 'misc', 'ngToast', 'app.directives'])

        .config(['ngToastProvider', function(ngToast) {
            ngToast.configure({
                dismissButton: false,
                dismissOnTimeout: false,
                dismissOnClick: false,
                animation: 'slide',
                horizontalPosition: 'right',
                verticalPosition: 'top',
                maxNumber: '1',
                additionalClasses: 'toast'
            });
        }])

        .run(['urlHandler', angular.noop])

    ;
}(window.angular));