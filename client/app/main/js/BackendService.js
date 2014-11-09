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

        this._setTowingETA = function(accidentID, towingETA){
            return $http({method:'POST', url:"/server/AccidentServlet", data:{accidentID:accidentID, towingETA:towingETA}, headers:{operation:"SET_TOWING_ETA"}});
        }

        this._setCarReplacementETA = function(accidentID, carReplacementETA){
            return $http({method:'POST', url:"/server/AccidentServlet", data:{accidentID:accidentID, carReplacementETA:carReplacementETA}, headers:{operation:"SET_CAR_REPLACEMENT_ETA"}});
        }

        this._setClaimSent = function(accidentID, claimSentToInsurance){
            return $http({method:'POST', url:"/server/AccidentServlet", data:{accidentID:accidentID, claimSentToInsurance:claimSentToInsurance}, headers:{operation:"SET_CLAIM_SENT"}});
        }

        this._setClaimStatus = function(accidentID, claimStatus){
            return $http({method:'POST', url:"/server/AccidentServlet", data:{accidentID:accidentID, claimStatus:claimStatus}, headers:{operation:"SET_CLAIM_STATUS"}});
        }

        this.saveOpenClaim = function(accidentID, towingETA, carReplacementETA, claimSentToInsurance, claimStatus){


            /** This is very ugly code **/

            var towinPromise;
            var carReplacmentPromise

            if(towingETA !== "")
                towinPromise = this._setTowingETA(accidentID, towingETA);
            if(carReplacementETA !== "")
                carReplacmentPromise = this._setCarReplacementETA(accidentID, carReplacementETA);

            var claimsSentPromise = this._setClaimSent(accidentID, claimSentToInsurance);
            var calimsStatusPromise = this._setClaimStatus(accidentID, claimStatus);

            if(towinPromise && carReplacmentPromise)
                return Q.all([towinPromise, carReplacmentPromise, claimsSentPromise, calimsStatusPromise]);
            if(towinPromise && !carReplacmentPromise)
                return Q.all([towinPromise, claimsSentPromise, calimsStatusPromise]);
            if(!towinPromise && carReplacmentPromise)
                return Q.all([carReplacmentPromise, claimsSentPromise, calimsStatusPromise]);
            if(!towinPromise && !carReplacmentPromise)
                return Q.all([claimsSentPromise, calimsStatusPromise]);
        }
    }
);