function sessionOut(app) {
     app
    // 未登录或会话过期时转向到登录页面
    .factory('sessionExpireInterceptor', ['$q', '$location', function($q, $location) {
        var path = $location.absUrl();
        var pathArray = path.split('/');
        var appContext = pathArray[3];

        return {
            responseError: function(error) {
                if (error.status == 401 || error.status === 403) {
                    window.location.href = '/' + appContext + '/qic/login';
                }
                return $q.reject(error);
            }
        };
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('sessionExpireInterceptor');
    }])
;
}