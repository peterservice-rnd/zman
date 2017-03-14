(function (require, module) {
    'use strict';
    var
        gulp = require('gulp'),
        compile = require('gulp-angular-templatecache')
    ;
    module.exports = function () {
        this.getPaths = function () {
            return ['src/templates/**/*'];
        };
        this.stream = function () {
            return gulp.src(this.getPaths())
                .pipe(compile('templates.js', {
                    standalone: true,
                    moduleSystem: 'IIFE', // Don't litter global scope
                    transformUrl: function (url) {
                        return url.replace(/\.tpl\.html$/, '');
                    }
                }))
            ;
        };
    };
}(require, module));
