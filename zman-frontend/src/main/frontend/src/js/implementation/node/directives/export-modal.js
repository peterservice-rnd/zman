(function (angular) {
    'use strict';

    angular.module('node.directives')
        .directive('exportNodeModal', function () {
            return {
                restrict: 'E',
                controller: ExportController,
                controllerAs: 'exportCtrl',
                templateUrl: 'modal/node/export'
            };
        });

    ExportController.$inject = ['nodeStorage', 'url'];
    function ExportController(storage, url) {
        var
            exportLink = url.create('api/zk/'),
            exportArg = '?export=true',
            formatArg = '&format='
        ;

        this.getNodeName = function () {
            return storage.getPath();
        };

        this.getExportLink = function (format) {
            var link = exportLink + storage.getAlias();
            if (storage.getPath() !== null && storage.getPath() !== '/') {
                link += storage.getPath().split('/').map(encodeURIComponent).join('/');
            }

            return link + exportArg + formatArg + format;
        };
    }
}(window.angular));