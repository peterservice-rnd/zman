(function (angular) {
    'use strict';

    angular.module('node.services')
        .service('nodeUnitOfWork', NodeUnitOfWork);

    NodeUnitOfWork.$inject = [];
    function NodeUnitOfWork () {
        var CLEAN = 'clean',
            DIRTY = 'dirty',
            self = this,
            init
        ;

        init = function (node) {
            if (node._uow === null || typeof node._uow !== 'string') {
                self.markDirty(node);
            }
        };

        this.markDirty = function (node) {
            node._uow = DIRTY;
            return node;
        };
        this.markClean = function (node) {
            node._uow = CLEAN;
            return node;
        };
        this.isDirty = function (node) {
            init(node);
            return node._uow === DIRTY;
        };
        this.isNew = function (node) {
            return !node.hasOwnProperty('_uow');
        };
        this.isClean = function (node) {
            init(node);
            return node._uow === CLEAN;
        };
    }
}(window.angular));