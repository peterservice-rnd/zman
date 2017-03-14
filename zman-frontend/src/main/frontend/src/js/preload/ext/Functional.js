// jshint freeze: false
(function (Array) {
    'use strict';
    Array.prototype.drop = function (numberOfElementsToDrop) {
        return this.slice(0, this.length - numberOfElementsToDrop);
    };
    Array.prototype.skip = function (numberOfElementsToSkip) {
        return this.slice(numberOfElementsToSkip);
    };
    Array.prototype.head = function () {
        return this[0];
    };
    Array.prototype.tail = function () {
        return this.slice(1);
    };
    Array.prototype.init = function () {
        return this.slice(0, this.length - 1);
    };
    Array.prototype.last = function () {
        return this[this.length - 1];
    };
}(window.Array));
