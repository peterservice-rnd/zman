(function (require, module) {
    'use strict';
    var
        del = require('del'),
        gulp = require('gulp'),
        list = require('./list'),
        csso = require('gulp-csso'),
        concat = require('gulp-concat')
    ;
    module.exports = function (taskPrefix, targetFile) {
        if (taskPrefix === undefined) {
            taskPrefix = 'vendors.css';
        }
        if (targetFile === undefined) {
            targetFile = 'vendors.css';
        }

        gulp.task(taskPrefix + '.clean', function () {
            del('assets/dist/' + targetFile);
        });

        gulp.task(taskPrefix + '.build', function () {
            return gulp.src(list)
                .pipe(concat(targetFile))
                .pipe(csso())
                .pipe(gulp.dest('assets/dist'))
            ;
        });

        gulp.task(taskPrefix, [taskPrefix + '.clean', taskPrefix + '.build']);

        gulp.task(taskPrefix + '.watch', function () {
            return gulp.watch(list, [taskPrefix]);
        });
    };
}(require, module));
