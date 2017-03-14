(function (require, module) {
    'use strict';
    var
        del = require('del'),
        gulp = require('gulp'),
        list = require('./list'),
        uglify = require('gulp-uglify'),
        concat = require('gulp-concat')
    ;
    module.exports = function (taskPrefix, targetFile) {
        if (taskPrefix === undefined) {
            taskPrefix = 'vendors.js';
        }
        if (targetFile === undefined) {
            targetFile = 'vendors.js';
        }

        gulp.task(taskPrefix + '.clean', function () {
            del(targetFile);
        });

        gulp.task(taskPrefix + '.build', function () {
            return gulp.src(list)
                .pipe(concat(targetFile))
                .pipe(uglify())
                .pipe(gulp.dest('assets/dist'))
            ;
        });

        gulp.task(taskPrefix, [taskPrefix + '.clean', taskPrefix + '.build']);

        gulp.task(taskPrefix + '.watch', function () {
            gulp.watch(list, [taskPrefix]);
        });
    };
}(require, module));
