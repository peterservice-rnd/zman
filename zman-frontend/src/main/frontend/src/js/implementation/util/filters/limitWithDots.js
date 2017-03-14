(function (angular) {
    "use strict";
    angular.module('util.filters')
        .filter('limitWithDots', function () {
            return function (text, limit) {
                if (text === null) {
                    return '';
                }
                if (limit === undefined) {
                    limit = 100;
                }
                if (typeof text !== 'string') {
                    text = text.toString();
                }
                if (text.length > limit) {
                    text = text.substring(0, limit) + '...';
                }
                return text;
            };
        })
    ;
}(window.angular));
