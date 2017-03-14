(function (angular, Modernizr, location, addEventListener, decodeURI, encodeURI) {
    'use strict';

    if (!Modernizr.firefox) {
        decodeURI = function (ret) {
            return ret;
        };
        encodeURI = decodeURI;
    }

    angular.module('util.services', ['appConfig'])
        .service('url', Url);

    Url.$inject = ['configuration'];
    function Url(config) {
        var
            lastNode,
            setHash = function (hash) {
                location.hash = '/' + encodeURI(hash);
            }
        ;

        this.getPrefix = function () {
            return location.origin + ('/' + config.contextPath + '/').replace(/\/+/g, '/');
        };

        this.create = function (url) {
            return this.getPrefix() + url;
        };

        this.getAlias = function () {
            var alias = location.hash.split('/').tail().head();
            if (angular.isString(alias) && alias !== '') {
                return decodeURI(alias);
            }
            return null;
        };
        this.getNode = function () {
            var node = location.hash.split('/').skip(2).join('/');
            if (angular.isString(node)) {
                return decodeURI(node);
            }
            return '';
        };

        this.needRefresh = function () {
            location.hash = location.hash.replace(/\/+/g, '/').replace(/\/$/g, '');
            var currentNode = '/' + this.getNode(), need = currentNode !== lastNode;
            lastNode = currentNode;
            return need;
        };

        this.getPaths = function () {
            var tempPath = '', paths = [];
            this.getNode().split('/').init().forEach(function (key) {
                tempPath = tempPath + '/' + key;
                paths.push(tempPath);
            });
            return paths;
        };

        this.hasAlias = function () {
            return angular.isString(this.getAlias());
        };

        this.updatePath = function (path) {
            lastNode = path;
            setHash(this.getAlias() + path);
        };

        this.updateAlias = setHash;

        lastNode = this.getNode();
    }

}(window.angular, window.Modernizr, window.location, window.addEventListener, decodeURI, encodeURI));