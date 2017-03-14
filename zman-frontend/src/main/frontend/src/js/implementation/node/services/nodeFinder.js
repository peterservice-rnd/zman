(function (angular) {
    'use strict';

    angular.module('node.services')
        .service('nodeFinder', NodeFinder);

    NodeFinder.$inject = ['nodeStorage'];
    function NodeFinder(storage) {
        var currentNode = null, purifyNodeName, searchNode;

        this.locateParent = function (path) {
            var parentPath = path.split('/').drop(1).join('/'),
                parent = this.find(parentPath);

            if (parent === null) {
                parent = this.locateParent(parentPath);
            }
            return parent;
        };

        purifyNodeName = function (name) {
            name += '';
            name = name.replace(/\/+/, '/');
            if (name.endsWith('/')) {
                name = name.substring(0, name.length - 1);
            }
            if (name.startsWith('/')) {
                name = name.substring(1);
            }
            return name;
        };

        searchNode = function (path, comparator) {
            var nodeKey, found, traversalNode = currentNode;
            if (typeof comparator !== 'function') {
                comparator = function (name, path) {
                    return purifyNodeName(name) === purifyNodeName(path);
                };
            }
            if (comparator(currentNode.znodeName, path)) {
                return currentNode;
            }
            if (currentNode.children === null || currentNode.children === undefined) {
                return null;
            }
            for (nodeKey in traversalNode.children) {
                if (traversalNode.children.hasOwnProperty(nodeKey)) {
                    currentNode = traversalNode.children[nodeKey];
                    if ((found = searchNode(path)) !== null) {
                        return found;
                    }
                }
            }
            return null;
        };

        this.find = function (path) {
            currentNode = storage.getNodes()[0];
            return searchNode(path, function (name, path) {
                return purifyNodeName(name).toLowerCase() === purifyNodeName(path).toLowerCase();
            });
        };
    }
}(window.angular));