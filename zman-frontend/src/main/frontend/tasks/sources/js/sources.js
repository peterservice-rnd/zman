(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        sources = require('./list.js')
    ;
    module.exports = function () {
        this.getPaths = function () {
            return sources;
        };
        this.stream = function () {
            return gulp.src(sources);
        };
    };
}(require, module));
