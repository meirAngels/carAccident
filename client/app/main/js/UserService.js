reportingApp.service('userSrv',function($http) {

        this.currentUser;

        this.whoAmI = function() {

            var me = this;

            var deferred = Q.defer();

            if(me.currentUser) {
                deferred.resolve(me.currentUser);
            } else {

                $http.get("/server/UserServlet").
                    success(function (data, status, headers, config) {
                        me.currentUser = data;
                        deferred.resolve(data);
                    }).
                    error(function (data, status, headers, config) {
                        deferred.reject(data);
                    });

            }

            return deferred.promise;

        }

    }
);