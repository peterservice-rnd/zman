(function (angular) {
    'use strict';
    angular.module('node.directives')
        .directive('importNodeModal', function () {
            return {
                restrict: 'E',
                controller: ImportController,
                controllerAs: 'importCtrl',
                templateUrl: 'modal/node/import'
            };
        });

    ImportController.$inject = ['Upload', 'nodeStorage', 'treeHandler', 'nodeUtils', 'toast'];
    function ImportController(Upload, nodeStorage, treeHandler, nodeUtils, toast) {
        var
            self = this,
            upload
        ;

        this.getNodeName = function () {
            return nodeStorage.getPath();
        };

        this.submit = function () {
            if (this.file) {
                upload(this.file);
            }
        };

        upload = function (file) {
            var config, format = file.name.split('.').last().toLowerCase();

            config = {
                url: 'api/zk/' + nodeStorage.getAlias() + treeHandler.get().znodeName,
                headers : {
                    'Content-Type': 'application/' + format
                  },
                data: file
            };

            if (self.overwrite) {
                config.url += '?overwrite=true';
            }

            Upload.http(config).then(function (resp) {
                self.file = null;
                nodeUtils.softRefresh(treeHandler.get());
                if (resp.data.conflicts.length > 0) {
                    var message = 'Created with conflicts: <br>' + resp.data.conflicts.join('<br>');
                    toast.warning(message);
                } else {
                    toast.success('Imported');
                }
            }, function (resp) {
                self.file = null;
                toast.error(resp.data.body);
            }, function () {
                toast.loading('Importing...');
            });
        };
    }
}(window.angular));