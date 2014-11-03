reportingApp.service('backendSrv',function($http) {

        this.signIn = function() {
            return 'boo';
        }

        this.reportAnAccident = function(data) {

            var deferred = Q.defer();

            $http.post("/server/AccidentServlet" , data).
                success(function(data, status, headers, config) {
                    deferred.resolve(data);
                }).
                error(function(data, status, headers, config) {
                    deferred.reject(data);
                });

            return deferred.promise;
        }

        this.getAccidentsList = function() {

            var deferred = Q.defer();

            $http.get("/server/AccidentServlet").
                success(function(data, status, headers, config) {
                    deferred.resolve(data);
                }).
                error(function(data, status, headers, config) {
                    deferred.reject(data);
                });

            return deferred.promise;
        }
    }
);