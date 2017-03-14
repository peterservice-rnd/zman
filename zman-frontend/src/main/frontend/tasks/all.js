(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        sources = require('./sources/all'),
        vendors = require('./vendors/all')
    ;
    module.exports = function () {
        sources();
        vendors();

        gulp.task('build', ['sources', 'vendors']);
        gulp.task('default', ['build']);
        gulp.task('watch', ['sources.watch', 'vendors.watch']);
    };
}(require, module));
