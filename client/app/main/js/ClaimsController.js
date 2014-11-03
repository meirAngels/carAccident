reportingControllers.controller('ClaimsCtrl',
    function ($scope, $location, backendSrv) {


        $scope.claimsArray = [{claimId:"claimTest1",name:"Joe Sobe", userName:"i070386", inProcess:true,completed:false,phoneNumber:0528962135, claimToInsurance:true, towingTruckSent:true, replacmentCarSent:false, medicalAssistanceSent:false},{claimId:"claimTest2",name:"Joe2 Sobe2", userName:"444", inProcess:false,completed:false,phoneNumber:666, claimToInsurance:false, towingTruckSent:false, replacmentCarSent:false, medicalAssistanceSent:false}]

        $scope.name;
        $scope.username;
        $scope.phoneNumber;
        $scope.claimToInsurance;
        $scope.towingTruckSent;
        $scope.inProcess;
        $scope.completed;

        $scope.claimClicked = function(claimId){
            var claimsArray = $scope.claimsArray;
            for(var i=0; i<claimsArray.length; i++)
            {
                if(claimsArray[i].claimId === claimId)
                {
                    $scope.name =  claimsArray[i].name;
                    $scope.username = claimsArray[i].userName;
                    $scope.phoneNumber = claimsArray[i].phoneNumber;
                    $scope.claimToInsurance = claimsArray[i].claimToInsurance;
                    $scope.towingTruckSent = claimsArray[i].towingTruckSent;
                    $scope.inProcess = claimsArray[i].inProcess;
                    $scope.completed = claimsArray[i].completed;
                }
            }
        }

        $scope.getClaims = function(){

        }

        $scope.callUser = function(){

        }

        $scope.$on('$routeChangeSuccess', function () {

            $scope.claimsArray = [];


            backendSrv.getAccidentsList().then(
                function(data) {
                    $scope.$apply(function() {
                        _.each(data.accidents, function (e, i) {
                            $scope.claimsArray.push(
                                {
                                    claimId: e.accidentId,
                                    name: "Meir Rotstein",
                                    userName: "i070386",
                                    inProcess: true,
                                    completed: false,
                                    phoneNumber: 0528962135,
                                    claimToInsurance: true,
                                    towingTruckSent: true,
                                    replacmentCarSent: false,
                                    medicalAssistanceSent: false
                                }
                            )
                        })
                    });


                },

                function(data) {

                }

            )
        });

    }
);
