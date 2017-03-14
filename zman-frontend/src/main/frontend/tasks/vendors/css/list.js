(function (module) {
    'use strict';
    var p = function (pkg, path) {
        return 'bower_components/' + pkg + '/' + path + '.css';
    };

    module.exports = [
        p('angular', 'angular-csp'),
        p('bootstrap', 'dist/css/bootstrap.min'),
        p('angular-ui-tree', 'dist/angular-ui-tree.min'),
        p('ngtoast', 'dist/ngToast.min'),
        p('ngtoast', 'dist/ngToast-animations.min'),
        p('flexboxgrid', 'css/flexboxgrid.min'),

        'src/shared/css/font-awesome.css',
        'src/shared/css/main.min.css',
        'src/shared/css/skins/darkblue.css'
    ];
}(module));
