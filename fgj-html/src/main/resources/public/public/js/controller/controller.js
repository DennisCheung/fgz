/**
 * Created by zhenyu on 2017/5/24.
 */

'use strict';

angular.module('app')
    .run(['$http','cache',function ($http,cache) {
        // 拿到上一个应用传入的teamId
        var str =  window.location.href;
        if (str.indexOf('=') > -1){

            var index1 = str.indexOf('=') + 1;
            if (str.indexOf('#') > -1) {
                var index2 = str.indexOf('#');
            }
            if (index2) {
                var ghbTeamId = str.slice(index1, index2);
            } else {
                var ghbTeamId = str.slice(str.indexOf('=') + 1);
            }

            cache.put('teamId',ghbTeamId);
        } else {
             //cache.put('teamId', 1000124);
            alert('没有teamId。');
        }
    }])

    /*--------------- 福管家主页 ----------------*/
    .controller('workstationHomePageCtrl', ['$scope', function ($scope) {
        $scope.homePageList = [
            {
                "url": "handBracelets",
                "text": "发放设备",
                "imgUrl": "../images/issue.png"
            },
            {
                "url": "recoveryplant",
                "text": "回收设备",
                "imgUrl": "../images/recycle.png"
            },
            {
                "url": "lookIndex",
                "text": "查看",
                "imgUrl": "../images/check01.png"
            }
        ];
    }])

    /*--------------- 发放手环 ----------------*/
    .controller('handBraceletsCtrl', ['$scope', 'cache', '$interval', '$http', '$window', function ($scope, cache, $interval, $http, $window) {
        // 弹窗控制
        $scope.show = false;
        $scope.infoPromit = false;
        $scope.load = false;

        // 定时器创建
        function createTime(num, state) {
            $scope.index = 3;
            if (num === 1) {
                $scope.infoPromit = true;
            } else if (num === 2) {
                $scope.show = true;
                if (num === 2) {
                    // 弹出框的内容
                    $scope.device = $scope.selectDevice.basicInfo.deviceModelName;
                    $scope.deviceType = $scope.selectDevice.basicInfo.deviceTypeName;
                    $scope.personname = $scope.selectCustomerInfo.basicInfo.personName;
                }
            }

            // 定时关闭弹窗
            var timmer = $interval(function () {

                $scope.index --;
                if ($scope.index === 0) {
                    if (num === 1) {
                        $scope.infoPromit = false;
                        if (state === 'myerror') {
                            $window.location.reload();
                        }
                        $interval.cancel(timmer);
                    } else if (num === 2) {
                        $scope.show = false;
                        $interval.cancel(timmer);
                        window.location.reload();
                    }
                }
            }, 1000);
        }

        // 判断客户是否选择客户，设备，备注
        if (sessionStorage.getItem('selectInfo')) {

            // 客户
            $scope.selectCustomerInfo = JSON.parse(sessionStorage.getItem("selectInfo"));
            $scope.selectCustomerInfo.basicInfo.idNo === null ? $scope.selectCustomerInfo.basicInfo.idNo = '' : $scope.selectCustomerInfo.basicInfo.idNo;
        }
        if (sessionStorage.getItem('selectDevice')) {

            // 设备
            $scope.selectDevice = JSON.parse(sessionStorage.getItem("selectDevice"));
            console.log($scope.selectDevice);
        }
        if (sessionStorage.getItem('remark')) {

            // 备注
            $scope.remark = sessionStorage.getItem("remark");
        }

        // 发放点击
        $scope.provide = function () {

            // 判断是否选择客户
            if (!sessionStorage.getItem('selectInfo')) {
                $scope.promitInfo = '请选择客户，再发放。';
                createTime(1, '');
                return;
            }

            // 判断是否选择设备
            if (!sessionStorage.getItem('selectDevice')) {
                $scope.promitInfo = '请选择设备，再发放。';
                createTime(1, '');
                return;
            }

            // $scope.load = true;

            /*
            * 发放
            * TEH，WRISTBAND这两种设备发放跟回收调用一样的接口，HMO自己拥有独立的发放跟回收接口
            */
            if ($scope.selectDevice.basicInfo.deviceTypeId === "WRISTBAND" || $scope.selectDevice.basicInfo.deviceTypeId === "TEH") {
                $http({
                    url: '../../device/deviceIssue',
                    method: 'POST',
                    data: {
                        "personId": $scope.selectCustomerInfo.id,
                        "deviceSn": $scope.selectDevice.basicInfo.deviceSn,
                        "teamId": cache.get("teamId"),
                        "userId": '',
                        "issueDesc": $scope.remark
                    }
                }).then(function (resp) {
                    $scope.load = false;
                    // 弹窗
                    createTime(2, '');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                }).catch(function (err) {
                    $scope.load = false;
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                    return;
                });
            } else if ($scope.selectDevice.basicInfo.deviceTypeId === "HMO") {
                $http({
                    url: '../../device/grantDeviceHmoIssue',
                    method: 'POST',
                    data: {
                        "personId": $scope.selectCustomerInfo.id,
                        "deviceSn": $scope.selectDevice.basicInfo.deviceSn,
                        "deviceId": $scope.selectDevice.id,
                        "teamId": cache.get("teamId"),
                        "userId": '',
                        "issueDesc": $scope.remark,
                        "isDelete": $scope.selectDevice.usedInfo.isUsed,
                        "userNo": $scope.selectDevice.userNo
                    }
                }).then(function (resp) {
                    $scope.load = false;
                    // 弹窗
                    createTime(2, '');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                }).catch(function (err) {
                    $scope.load = false;
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                    return;
                });
            } else if ($scope.selectDevice.basicInfo.deviceTypeId === "CALL") {
                $http({
                    url: '../../device/devcallissue',
                    method: 'POST',
                    data: {
                        "familyId": $scope.selectCustomerInfo.familyInfo.familyId,
                        "deviceSn": $scope.selectDevice.basicInfo.deviceSn,
                        "teamId": cache.get("teamId"),
                        "userId": '',
                        "desc": $scope.remark
                    }
                }).then(function (resp) {
                    $scope.load = false;
                    // 弹窗
                    createTime(2, '');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                }).catch(function (err) {
                    $scope.load = false;
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('selectInfo');
                    sessionStorage.removeItem('selectDevice');
                    sessionStorage.removeItem('remark');
                    return;
                });
            }
        };
    }])

    /*--------------- 选择客户 ----------------*/
    .controller('selectCustomerCtrl', ['$scope', '$state', 'cache', function ($scope, $state, cache) {

        // 状态
        $scope.state = $state.params.state;
        $scope.clearHistory = function () {
            $scope.historyList = [];
        };

        $scope.selectMember = function (item) {
            window.history.back();
        };
    }])

    /*--------------- 搜索界面 ----------------*/
    .controller('searchCtrl', ['$scope', '$http', 'cache', 'scrollList', '$state', '$q', '$interval', function ($scope, $http, cache, scrollList, $state, $q, $interval) {

        $scope.infoPromit = false;
        // 定时器创建
        function createTime(num) {
            $scope.index = 3;
            if (num === 1) {
                $scope.infoPromit = true;
            } else if (num === 2) {
                $scope.show = true;
                if (num === 2) {
                    // 弹出框的内容
                    $scope.device = $scope.selectDevice.basicInfo.deviceModelName;
                    $scope.deviceType = $scope.selectDevice.basicInfo.deviceTypeName;
                    $scope.personname = $scope.selectCustomerInfo.basicInfo.personName;
                }
            }

            // 定时关闭弹窗
            var timmer = $interval(function () {

                $scope.index --;
                if ($scope.index === 0) {
                    if (num === 1) {
                        $scope.infoPromit = false;
                        $interval.cancel(timmer);
                    } else if (num === 2) {
                        $scope.show = false;
                        $interval.cancel(timmer);
                        window.location.reload();
                    }
                }
            }, 1000);
        }

        $scope.cancleClick = function () {
            $scope.searchText = '';
        };
        $scope.state = $state.params.state;
        // 选择的人员
        $scope.selectMember = function (item) {
            if ($state.params.state === 'provide') {
                sessionStorage.setItem('selectInfo', JSON.stringify(item));
            }
            window.history.go(-2);
        };

        $scope.searchClick = function () {
            // 查询搜索成员
            $http.get('../../fgj/query/personbytext?teamId='+cache.get('teamId')+'&text='+$scope.searchText).then(function (resp) {
                $scope.personList = resp.data;
                if ($scope.personList.length === 0) {
                    createTime(1);
                }
            }).catch(function (err) {
                alert(err.data.error);
            });
        };
    }])

    /*--------------- 选择设备 ----------------*/
    .controller('selectDeviceCtrl', ['$scope', '$state', 'cache', function ($scope, $state, cache) {
        $scope.state = $state.params.state;
        $scope.clearHistory = function () {
            $scope.historyList = [];
        };

        $scope.selectMember = function (item) {
            window.history.back();
        };
    }])

    /*--------------- 搜索设备界面 ----------------*/
    .controller('searchDeviceCtrl', ['$scope', '$http', 'scrollList', '$state', '$q', '$interval', 'cache', function ($scope, $http, scrollList, $state, $q, $interval, cache) {

        $scope.infoPromit = false;
        // 定时器创建
        function createTime(num) {
            $scope.index = 3;
            if (num === 1) {
                $scope.infoPromit = true;
            } else if (num === 2) {
                $scope.show = true;
                if (num === 2) {
                    // 弹出框的内容
                    $scope.device = $scope.selectDevice.basicInfo.deviceModelName;
                    $scope.deviceType = $scope.selectDevice.basicInfo.deviceTypeName;
                    $scope.personname = $scope.selectCustomerInfo.basicInfo.personName;
                }
            }

            // 定时关闭弹窗
            var timmer = $interval(function () {

                $scope.index --;
                if ($scope.index === 0) {
                    if (num === 1) {
                        $scope.infoPromit = false;
                        $interval.cancel(timmer);
                    } else if (num === 2) {
                        $scope.show = false;
                        $interval.cancel(timmer);
                        window.location.reload();
                    }
                }
            }, 1000);
        }

        $scope.cancleClick = function () {
            $scope.deviceInfo = '';
        };

        // 选择设备
        $scope.selectDevice = function (item) {
            if ($state.params.state === 'provide') {
                sessionStorage.setItem('selectDevice', JSON.stringify(item));
            } else if ($state.params.state === 'recover') {  // 回收
                sessionStorage.setItem('recoverDevice', JSON.stringify(item));
            }
            window.history.go(-2);
        }

        $scope.searchClick = function () {

            if ($state.params.state === 'recover') {  // 回收
                // 查询搜索成员
                $http.get('../../fgj/query/deviceAllUsed?snStr='+$scope.deviceInfo+'&teamId='+cache.get("teamId")).then(function (resp) {
                    handleObj(resp.data);
                }).catch(function (err) {
                    alert(err.data.error);
                });
            } else if ($state.params.state === 'provide') {
                // 查询搜索成员
                $http.get('../../fgj/query/deviceUnUsed?snStr='+$scope.deviceInfo+'&teamId='+cache.get("teamId")).then(function (resp) {
                    handleObj(resp.data);
                }).catch(function (err) {
                    alert(err.data.error);
                });
            }

            // then里面的第一个参数对应 def.resolve(result.data);
            function handleObj (obj) {
                $scope.serviceList = obj;
                angular.forEach($scope.serviceList, function (item, index) {
                    if (item.basicInfo.deviceModelName.length + item.basicInfo.deviceTypeName.length + item.basicInfo.deviceSn.length > 30) {
                        $scope.serviceList[index].line = true;
                    } else {
                        $scope.serviceList[index].line = false;
                    }
                });
                if ($scope.serviceList.length === 0) {
                    createTime(1);
                }
            }
        };
    }])

    /*--------------- 备注 ----------------*/
    .controller('remarkCtrl', ['$scope', '$state', function ($scope, $state) {
        $scope.textLength = 0;

        // 监听输入字数
        $scope.$watch('remark',function(){
            if ($scope.remark) {
                $scope.textLength = $scope.remark.length;
            } else {
                $scope.textLength = 0;
            }
        });

        // 保存
        $scope.saveClick = function () {
            if ($state.params.state === 'recover') {  // 回收
                sessionStorage.setItem('recoverRemark', $scope.remark);
            } else if ($state.params.state === 'provide') {
                sessionStorage.setItem('remark', $scope.remark);
            }
            window.history.back();
        };
    }])

    /*--------------- 回收设备 ----------------*/
    .controller('recoveryplantCtrl', ['$scope', '$http', 'cache', '$interval', '$window', function ($scope, $http, cache, $interval, $window) {
        // 弹窗控制
        $scope.show = false;
        $scope.infoPromit = false;

        // 定时器创建
        function createTime(num, state) {
            $scope.index = 3;
            if (num === 1) {
                $scope.infoPromit = true;
            } else if (num === 2) {
                $scope.show = true;
                $scope.device = $scope.recoverDevice.basicInfo.deviceModelName;
                $scope.type = $scope.recoverDevice.basicInfo.deviceTypeName;
                $scope.personname = $scope.recoverPerson.personName || $scope.recoverPerson.famPerson;
            }

            // 定时关闭弹窗
            var timmer = $interval(function () {
                $scope.index --;
                if ($scope.index === 0) {
                    if (num === 1) {
                        $scope.infoPromit = false;
                        if (state === 'myerror') {
                            $window.location.reload();
                        }
                        $interval.cancel(timmer);
                    } else if (num === 2) {
                        $scope.show = false;
                        $interval.cancel(timmer);
                        window.location.reload();
                    }
                }
            }, 1000);
        }

        // 判断客户是否选择设备，备注
        if (sessionStorage.getItem('recoverDevice')) {

            // 设备
            $scope.recoverDevice = JSON.parse(sessionStorage.getItem("recoverDevice"));
            if (sessionStorage.getItem('recoverPerson')) {
                $scope.recoverPerson = JSON.parse(sessionStorage.getItem('recoverPerson'));
            } else {
                if ($scope.recoverDevice) {
                    if ($scope.recoverDevice.devPersonList) {
                        if ($scope.recoverDevice.devPersonList.length > 0) {
                            $scope.recoverPerson = $scope.recoverDevice.devPersonList[0];
                        }
                    }
                }
            }
        }
        if (sessionStorage.getItem('recoverRemark')) {

            // 备注
            $scope.remark = sessionStorage.getItem("recoverRemark");
        }

        // 回收
        $scope.recoverClick = function () {

            // 判断是否选择设备
            if (!sessionStorage.getItem('recoverDevice')) {
                $scope.promitInfo = '请选择设备，再回收。';
                createTime(1, '');
                return;
            }

            /*
             * 回收
             * TEH，WRISTBAND这两种设备发放跟回收调用一样的接口，HMO自己拥有独立的发放跟回收接口
             */
            if ($scope.recoverDevice.basicInfo.deviceTypeId === "WRISTBAND" || $scope.recoverDevice.basicInfo.deviceTypeId === "TEH") {
                $http({
                    url: '../../device/deviceRecycle',
                    method: 'POST',
                    data: {
                        "personId": $scope.recoverPerson.personId,
                        "deviceSn": $scope.recoverDevice.basicInfo.deviceSn,
                        "teamId": cache.get("teamId"),
                        "userId": ''
                    }
                }).then(function (resp) {

                    // 定时器
                    createTime(2, '');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson');
                }).catch(function (err) {
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson');
                    return;
                });
            } else if ($scope.recoverDevice.basicInfo.deviceTypeId === "HMO") {
                $http({
                    url: '../../device/recoverDeviceHmoIssue',
                    method: 'POST',
                    data: {
                        "personId": $scope.recoverPerson.personId,
                        "deviceSn": $scope.recoverDevice.basicInfo.deviceSn,
                        "deviceId": $scope.recoverDevice.id,
                        "teamId": cache.get("teamId"),
                        "userId": '',
                        "issueDesc": $scope.remark,
                        "isDelete": '',
                        "userNo": $scope.recoverDevice.userNo
                    }
                }).then(function (resp) {

                    // 定时器
                    createTime(2, '');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson')
                }).catch(function (err) {
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson');
                    return;
                });
            } else if ($scope.recoverDevice.basicInfo.deviceTypeId === "CALL") {
                $http({
                    url: '../../device/recycleCall',
                    method: 'POST',
                    data: {
                        "familyId": $scope.recoverDevice.devPersonList[0].familyId,
                        "deviceSn": $scope.recoverDevice.basicInfo.deviceSn,
                        "teamId": cache.get("teamId"),
                        "userId": ''
                    }
                }).then(function (resp) {

                    // 定时器
                    createTime(2, '');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson')
                }).catch(function (err) {
                    $scope.promitInfo = err.data.error;
                    createTime(1, 'myerror');
                    sessionStorage.removeItem('recoverDevice');
                    sessionStorage.removeItem('recoverRemark');
                    sessionStorage.removeItem('recoverPerson');
                    return;
                });
            }
        };
    }])

    /*--------------- 成员列表 ----------------*/
    .controller('personListCtrl', ['$scope', function ($scope) {
        if (sessionStorage.getItem("recoverDevice")) {
            $scope.deviceList = JSON.parse(sessionStorage.getItem("recoverDevice")).devPersonList;
        }
        $scope.selectPerson = function (item) {
            sessionStorage.setItem('recoverPerson', JSON.stringify(item));
            window.history.back();
        };
    }])

    /*--------------- 查看主页 ----------------*/
    .controller('lookIndexCtrl', ['$scope', function ($scope) {
    }])

    /*--------------- 查看主页 ----------------*/
    .controller('lookReleaseRecordsCtrl', ['$scope', '$state', 'cache', '$http', '$interval', function ($scope, $state, cache, $http, $interval) {
        $scope.infoPromit = false;
        // 定时器创建
        function createTime(num) {
            $scope.index = 3;
            if (num === 1) {
                $scope.infoPromit = true;
            } else if (num === 2) {
                $scope.show = true;
                $scope.device = $scope.recoverDevice.basicInfo.deviceModelName;
                $scope.type = $scope.recoverDevice.basicInfo.deviceTypeName;
                $scope.personname = $scope.recoverPerson.personName;
            }

            // 定时关闭弹窗
            var timmer = $interval(function () {
                $scope.index --;
                if ($scope.index === 0) {
                    if (num === 1) {
                        $scope.infoPromit = false;
                        $interval.cancel(timmer);
                    } else if (num === 2) {
                        $scope.show = false;
                        $interval.cancel(timmer);
                        window.location.reload();
                    }
                }
            }, 1000);
        }

        // 判断用户点击的发放还是回收
        if ($state.params.states === 'fafang') {

            // 发放
            $http.get('../../fgj/query/devissuenote?teamId=' + cache.get('teamId')).then(function (resp) {
                $scope.provideList = resp.data;
                if ($scope.provideList.length === 0) {
                    createTime(1);
                }
            }).catch(function (err) {
                alert(err.data.error);
            });
        } else if ($state.params.states === 'huishou') {

            // 回收
            $http.get('../../fgj/query/devrecyclenote?teamId=' + cache.get('teamId')).then(function (resp) {
                $scope.recycleList = resp.data;
                if ($scope.recycleList.length === 0) {
                    createTime(1);
                }
            }).catch(function (err) {
                alert(err.data.error);
            });
        }
    }]);
