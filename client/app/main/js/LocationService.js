reportingApp.service('locationSrv',function($http) {

        this.getCurrentPosition = function() {
            var deferred = Q.defer();

            if (navigator.geolocation) navigator.geolocation.getCurrentPosition(

                function(result) {
                    deferred.resolve(result);
                },

                function(err){
                    if(!err) {
                        deferred.reject('Cannot fetch your location - unexpected error');
                    } else {
                        deferred.reject(err);
                    }
                });

            return deferred.promise;
        }
    }
);