/**
 * Created by zhenyu on 2017/5/24.
 */

'use strict';

var app = angular.module('app')

    /*
     *  cookie的使用
     */
    .service('cache', ['$cookies', function ($cookies) {
        // 添加cookies
        this.put = function (key, value) {
            $cookies.put(key, value);
        };

        // 获取cookies
        this.get = function (key) {
            return $cookies.get(key);
        };

        // 删除cookies
        this.remove = function (key) {
            $cookies.remove(key);
        };

    }])

    /*
     *  列表滚动
     */
    .service('scrollList', [ function () {

        this.list = function (id) {
            var deviceList = document.getElementById(id);
            var searchHeight = document.getElementsByClassName('select-search-wapper')[0].clientHeight;
            var titleHeight = document.getElementsByClassName('search-title')[0].clientHeight;
            var height = document.body.clientHeight - searchHeight - titleHeight;
            deviceList.style.height = height + "px";
            var scroll = new window.BScroll(deviceList, {click: true});
            scroll.on('scroll', function (pos) {});
            scroll.scrollTo(0, 0);
        };
    }]);