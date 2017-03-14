(function (require, module) {
    'use strict';
    var
        del = require('del'),
        gulp = require('gulp'),
        less = require('gulp-less'),
        csso = require('gulp-csso'),
        concat = require('gulp-concat'),
        autoprefixer = require('gulp-autoprefixer'),

        path = 'src/less/**/*'
    ;
    module.exports = function (taskPrefix, targetFile) {
        if (taskPrefix === undefined) {
            taskPrefix = 'sources.less';
        }
        if (targetFile === undefined) {
            targetFile = 'sources.css';
        }

        gulp.task(taskPrefix + '.clean', function () {
            del('assets/dist/' + targetFile);
        });

        gulp.task(taskPrefix + '.build', function () {
            return gulp.src(path)
                .pipe(less())
                .pipe(autoprefixer())
                .pipe(concat(targetFile))
                .pipe(csso())
                .pipe(gulp.dest('assets/dist'))
            ;
        });

        gulp.task(taskPrefix, [taskPrefix + '.clean', taskPrefix + '.build']);

        gulp.task(taskPrefix + '.watch', function () {
            gulp.watch(path, [taskPrefix]);
        });
    };
}(require, module));
