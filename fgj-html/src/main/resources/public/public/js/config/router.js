/**
 * Created by zhenyu on 2017/5/24.
 */

'use strict';


angular.module('app')
    .config(['$stateProvider','$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

        $stateProvider

            /*--------------- 福管家 ----------------*/
            .state('workstationHomePage', {
                url: '/workstationHomePage',
                templateUrl: 'view/fgz/agent-workstation-home-page.html',
                controller: 'workstationHomePageCtrl'
            })

            /*--------------- 发放手环 ----------------*/
            .state('handBracelets', {
                url: '/handBracelets',
                templateUrl: 'view/fgz/hand-Bracelets.html',
                controller: 'handBraceletsCtrl'
            })

            /*--------------- 选择客户 ----------------*/
            .state('selectCustomer', {
                url: '/selectCustomer/:state',
                templateUrl: 'view/fgz/customer-choice.html',
                controller: 'selectCustomerCtrl'
            })

            /*--------------- 搜索客户 ----------------*/
            .state('search', {
                url: '/search/:state',
                templateUrl: 'view/fgz/search.html',
                controller: 'searchCtrl'
            })

            /*--------------- 选择设备 ----------------*/
            .state('selectDevice', {
                url: '/selectDevice/:state',
                templateUrl: 'view/fgz/select-device.html',
                controller: 'selectDeviceCtrl'
            })

            /*--------------- 搜索设备 ----------------*/
            .state('searchDevice', {
                url: '/searchDevice/:state',
                templateUrl: 'view/fgz/search-device.html',
                controller: 'searchDeviceCtrl'
            })

            /*--------------- 备注 ----------------*/
            .state('remark', {
                url: '/remark/:state',
                templateUrl: 'view/fgz/remark.html',
                controller: 'remarkCtrl'
            })

            /*--------------- 发放设备 ----------------*/
            .state('recoveryplant', {
                url: '/recoveryplant',
                templateUrl: 'view/fgz/ecovery-plant.html',
                controller: 'recoveryplantCtrl'
            })

            /*--------------- 成员列表 ----------------*/
            .state('personList', {
                url: '/personList',
                templateUrl: 'view/fgz/person-list.html',
                controller: 'personListCtrl'
            })

            /*--------------- 查看主页 ----------------*/
            .state('lookIndex', {
                url: '/lookIndex',
                templateUrl: 'view/fgz/look-index.html',
                controller: 'lookIndexCtrl'
            })

            /*--------------- 发放记录 ----------------*/
            .state('lookReleaseRecords', {
                url: '/lookReleaseRecords/:states',
                templateUrl: 'view/fgz/release-records.html',
                controller: 'lookReleaseRecordsCtrl'
            });


        $urlRouterProvider.otherwise('workstationHomePage');
    }]);