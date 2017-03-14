(function (angular, addEventListener) {
    'use strict';
    angular.module('util.services', ['appConfig'])
        .service('urlHandler', UrlHandler);

    UrlHandler.$inject = ['serverConnection', 'url', 'aliasStorage', 'nodeFinder', 'nodeUtils', '$rootScope'];
    function UrlHandler(connection, url, aliasStorage, finder, nodeUtils, scope) {

        addEventListener('hashchange', function () {
            if (connection.getAlias() === null || url.getAlias() !== connection.getAlias().alias) {
                connection.setAlias(null);
                aliasStorage.startup();
            }
            if (url.hasAlias() && url.needRefresh()) {
                nodeUtils.forceRefresh();
            }
        });

        scope.$on('nodeLoaded', function (e, nodeName) {
            var found;
            if (url.getPaths().indexOf(nodeName) !== -1) {
                found = finder.find(nodeName);
                found.expand();
                return;
            }
            if ('/' + url.getNode() === nodeName) {
                found = finder.find(nodeName);
                found.select();
                found.expand();
            }
        });
    }

}(window.angular, window.addEventListener));