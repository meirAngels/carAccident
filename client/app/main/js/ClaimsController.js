reportingControllers.controller('ClaimsCtrl',
    function ($scope, $location, backendSrv) {



        $scope.claimsArray = []

        $scope.currentAccident;


        $scope.towingneededDisabled = false;
        $scope.carreplacementneededDisabled = false;

        $scope.showClaim = false;


        $scope.claimClicked = function(claimId){
            $scope.showClaim = true;
            var claimsArray = $scope.claimsArray;
            for(var i=0; i<claimsArray.length; i++)
            {
                if(claimsArray[i].accidentId === claimId)
                {
                    $scope.currentAccident = claimsArray[i];
                    $scope.towingneededDisabled = !($scope.currentAccident.towingneeded);
                    $scope.carreplacementneededDisabled = !($scope.currentAccident.carreplacementneeded);

                }
            }
        }

        $scope.getClaims = function(){

        }

        $scope.callUser = function(){

        }

        $scope.save = function(){

            backendSrv.saveOpenClaim($scope.currentAccident.accidentId, $scope.currentAccident.towingETA, $scope.currentAccident.carReplacementETA, $scope.currentAccident.claimSentToInsurance, $scope.currentAccident.claimStatus)
                .then(function(data, status, headers, config){

                })
                .fail(function(){

                })


        }

        $scope.$on('$routeChangeSuccess', function () {

            backendSrv.getAccidentsList().then(
                function(data) {
                    $scope.$apply(function() {
                        _.each(data.accidents, function (obj, i) {
                            obj =    {"accidents":[{name:"Joe Dow", userId:"i070385",phoneNumber:0528962135,"accidentId":121212,"date":"2014-10-27","description":"Meir crashed the bus in Nepal","geolocation":"26.5333 N, 86.7333 E",
                                "towingneeded":true,"claimStatus":"IN_PROCESS","claimSentToInsurance":false,"towingETA":"","carReplacementETA":"","carreplacementneeded":false,
                                "injuries":true,"thirdparty":[]}]}
                            var claimsArrary = obj.accidents;


                            $scope.claimsArray= claimsArrary;
                        })
                    });


                },

                function(data) {

                }

            )
        });

    }
);
