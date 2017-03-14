(function (angular, $, setTimeout, window) {
    'use strict';
    function AceControllerFactory() {
        this.create = function (modelName) {
            Controller.$inject = ['aceModelStorage', 'treeHandler', '$rootScope'];
            function Controller(storage, tree, rootScope) {
                var editor = null,
                    session = null,
                    fontSize = 14,
                    updateHeight,
                    element = $('#aceEditor'),
                    realEditor = element.children().first(),
                    aceLoaded;

                updateHeight = function () {
                    if (realEditor.height() > element.height() - 1) {
                        realEditor.height(element.height() - 10);
                    } else {
                        realEditor.height(element.height() - 1);
                    }
                    editor.resize();
                    requestAnimationFrame(updateHeight);
                };
                aceLoaded = function (_editor) {
                    editor = _editor;
                    editor.$blockScrolling = Infinity;
                    editor.setFontSize(fontSize);
                    session = _editor.getSession();
                    updateHeight();
                };

                this.getModes = function () {
                    return ['Text', 'Json', 'Xml', 'Groovy'];
                };
                this.mode = this.getModes()[0];

                this.model = function (m) {
                    if (arguments.length) {
                        storage.set(modelName, m);
                        return;
                    }
                    return storage.get(modelName);
                };

                this.modeChanged = function () {
                    if (this.mode === 'Text') {
                        session.setMode(undefined);
                        return;
                    }
                    session.setMode('ace/mode/' + this.mode.toLowerCase());
                };

                this.getOptions = function () {
                    return {
                        mode: 'Text',
                        useWrapMode: false,
                        showGutter: true,
                        firstLineNumber: 1,
                        onLoad: aceLoaded
                    };
                };
            }
            return Controller;
        };
    }

    window.AceControllerFactory = AceControllerFactory;
}(window.angular, window.$, window.setTimeout, window));