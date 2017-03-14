(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        js = require('./js/all'),
        less = require('./less/all')
    ;
    module.exports = function (taskPrefix) {
        if (taskPrefix === undefined) {
            taskPrefix = 'sources';
        }

        js(taskPrefix + '.js');
        less(taskPrefix + '.less');

        gulp.task(taskPrefix + '.watch', [taskPrefix + '.js.watch', taskPrefix + '.less.watch']);

        gulp.task(taskPrefix, [taskPrefix + '.js', taskPrefix + '.less']);
    };
}(require, module));
