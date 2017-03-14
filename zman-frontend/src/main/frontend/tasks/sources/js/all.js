(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        concat = require('gulp-concat'),
        del = require('del'),
        uglify = require('gulp-uglify'),
        merge = require('gulp-merge'),

        Sources = require('./sources'),
        sources = new Sources(),
        Templates = require('./templates'),
        templates = new Templates()
    ;
    module.exports = function (taskPrefix, targetFile) {
        if (taskPrefix === undefined) {
            taskPrefix = 'sources.js';
        }
        if (targetFile === undefined) {
            targetFile = 'sources.js';
        }

        gulp.task(taskPrefix + '.clean', function () {
            del('assets/dist/' + targetFile);
        });

        gulp.task(taskPrefix + '.build', function () {
            return merge(templates.stream(), sources.stream())
                .pipe(concat(targetFile))
                .pipe(uglify())
                .pipe(gulp.dest('assets/dist'))
            ;
        });

        gulp.task(taskPrefix, [taskPrefix + '.clean', taskPrefix + '.build']);

        gulp.task(taskPrefix + '.watch', function() {
            gulp.watch([].concat(
                sources.getPaths(),
                templates.getPaths()
            ), [taskPrefix]);
        });
    };
}(require, module));
