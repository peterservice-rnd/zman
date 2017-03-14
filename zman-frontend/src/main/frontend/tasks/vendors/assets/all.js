(function (require) {
    'use strict';
    var
        gulp = require('gulp'),
        src = {
            icons: 'src/shared/ico/*',
            fonts: 'src/shared/fonts/*'
        }
    ;

    module.exports = function (taskPrefix) {
        if (taskPrefix === undefined) {
            taskPrefix = 'vendors.assets';
        }

        gulp.task(taskPrefix + '.icons', function () {
            gulp.src(src.icons)
                .pipe(gulp.dest('assets/ico'));
        });
        gulp.task(taskPrefix + '.icons.watch', function () {
            gulp.watch(src.icons, [taskPrefix]);
        });

        gulp.task(taskPrefix + '.fonts', function () {
            gulp.src(src.fonts)
                .pipe(gulp.dest('assets/fonts'));
        });
        gulp.task(taskPrefix + '.fonts.watch', function () {
            gulp.watch(src, [taskPrefix]);
        });

        gulp.task(taskPrefix, [taskPrefix + '.fonts', taskPrefix + '.icons']);
        gulp.task(taskPrefix + '.watch', [taskPrefix + '.watch.fonts', taskPrefix + '.watch.icons']);
    };
}(require));