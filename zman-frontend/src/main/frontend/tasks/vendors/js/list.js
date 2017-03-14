(function (module) {
    'use strict';
    var p = function (pkg, file) {
        if (file === undefined) {
            file = pkg;
        }
        return 'bower_components/' + pkg + '/' + file + '.min.js';
    }, a = function (file) {
        return 'bower_components/ace-builds/src/' + file + '.js';
    };

    module.exports = [
        p('jquery/dist', 'jquery'),

        // Ace
        a('ace'),
        a('mode-groovy'),
        a('mode-json'),
        a('mode-xml'),

        // Angular
        p('angular'),
        p('angular-resource'),
        p('angular-sanitize'),
        p('angular-animate'),

        // Tree
        p('angular-ui-tree', 'dist/angular-ui-tree'),
        p('angular-recursion'),

        // File upload
        p('ng-file-upload', 'ng-file-upload-all'),

        // ngToast
        p('ngtoast', 'dist/ngToast'),

        // Bootstrap
        p('bootstrap/dist/js', 'bootstrap'),

        // Polyfill
        'bower_components/modernizr/modernizr.js',

        // Ace angular
        'bower_components/angular-ui-ace/ui-ace.js'
    ];
}(module));
