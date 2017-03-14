(function (Modernizr, window) {
    'use strict';
    window.requestAnimationFrame =
        window.requestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        window.webkitRequestAnimationFrame;

    Modernizr.addTest('firefox', function () {
        return !!navigator.userAgent.match(/firefox/i);
    });
}(window.Modernizr, window));