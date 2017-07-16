/**
 * Created by zhenyu on 2017/5/24.
 */

'use strict';

angular.module('app')

    /*---- 列表指令 ----*/
    .directive('arrow', [function () {
        return {
            // 以属性的方式使用指令
            restrict: 'A',
            replace: true,
            templateUrl: 'view/template/arrow.html',
            scope: {
            }
        };
    }])

    /*---- 时间定时 ----*/
    .directive('alertTime', ['$interval', function ($interval) {
        return {
            // 以属性的方式使用指令
            restrict: 'A',
            replace: true,
            templateUrl: 'view/template/alert-time.html',
            scope: {
                device: '=',
                type: '=',
                name: '=',
                index: '=',
                state: '='
            },
            controller: function ($scope) {}
        };
    }])

    /*---- 信息输入不完玩整体提示 ----*/
    .directive('infoPromit', ['$interval', function ($interval) {
        return {
            // 以属性的方式使用指令
            restrict: 'A',
            replace: true,
            templateUrl: 'view/template/info-promit.html',
            scope: {
                promit: '='
            },
            controller: function ($scope) {
            }
        };
    }])

    /*---- 信息输入不完玩整体提示 ----*/
    .directive('scrollList', ['$interval', function ($interval) {
        return {
            // 以属性的方式使用指令
            link: function (scope, element, attr) {
                if (scope.$last == true) {
                    var scroll = element[0].parentNode.parentNode;
                    var searchHeight = document.getElementsByClassName('select-search-wapper')[0].clientHeight;
                    var titleHeight = document.getElementsByClassName('search-title')[0].clientHeight;
                    var height = document.body.clientHeight - searchHeight - titleHeight;
                    scroll.style.height = height + "px";
                    var listscroll = new window.BScroll(scroll, {click: true});
                }
            }
        };
    }])

    /*---- loadding ----*/
    .directive('loadding', [function () {
        return {
            restrict: 'A',
            replace: true,
            templateUrl: 'view/template/loadding.html'
        }
    }]);