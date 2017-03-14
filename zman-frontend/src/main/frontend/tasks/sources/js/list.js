(function (module) {
    'use strict';
    var p = function (file) {
        return 'src/js/' + file + '.js';
    };
    module.exports = [
        p('preload/**/*'),
        p('definition/**/*'),
        p('implementation/**/*')
    ];
}(module));
