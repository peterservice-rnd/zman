(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        css = require('./css/all'),
        js = require('./js/all'),
        assets = require('./assets/all')
    ;
    module.exports = function (taskPrefix) {
        if (taskPrefix === undefined) {
            taskPrefix = 'vendors';
        }

        css(taskPrefix + '.css');
        js(taskPrefix + '.js');
        assets(taskPrefix + '.assets');

        gulp.task(taskPrefix, [taskPrefix + '.css', taskPrefix + '.js', taskPrefix + '.assets']);

        gulp.task(taskPrefix + '.watch', [taskPrefix + '.css.watch', taskPrefix + '.js.watch', taskPrefix + '.assets']);
    };
}(require, module));
