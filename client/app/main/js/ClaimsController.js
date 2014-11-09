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

            var currentDate = new Date();
            var year = currentDate.getFullYear();
            var day = currentDate.getDate();
            var month = currentDate.getMonth() + 1;
            var towingArray = $scope.currentAccident.towingETA.split(":");
            var carReplacementArray = $scope.currentAccident.carReplacementETA.split(":");

            var towingHour = towingArray[0];
            var towingMinute = towingArray[1];
            var carReplacementHour = carReplacementArray[0];
            var carReplacementMin = carReplacementArray[1];

            var towingETA;
            if(towingArray.length >1)
                towingETA = day+"/"+month+"/"+year+" "+towingHour+":"+towingMinute;
            else
                towingETA = "";
            var carReplacementETA;

            if(carReplacementArray.length >1)
                carReplacementETA = day+"/"+month+"/"+year+" "+carReplacementHour+":"+carReplacementMin;
            else
                carReplacementETA = "";

            backendSrv.saveOpenClaim($scope.currentAccident.accidentId, towingETA, carReplacementETA, $scope.currentAccident.claimSentToInsurance, $scope.currentAccident.claimStatus)
                .then(function(data, status, headers, config){
                    var test= 5;
                })
                .fail(function(){
                    var test = 5
                })


        }

        $scope.$on('$routeChangeSuccess', function (data) {
            
           backendSrv.getAccidentsList().then(
                function(data) {
                    $scope.$apply(function() {
                        var claimsArrary = data.accidents;
                        $scope.claimsArray= claimsArrary;
                    });


                }
            )
        });

    }
);
