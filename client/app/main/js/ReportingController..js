reportingControllers.controller('ReportingCtrl',
    function ($scope, backendSrv) {

        //data
        $scope.injuried = false;
        $scope.user = {id: "123" , fname : "Meir" , lname : "Rotstein" , email : "meir.rotstein@sap.com" , phone : "054-7488557" , confirmed : true};

        //ui bindings
        $scope.submitSucceded = false;
        $scope.submitFalied = false;


        $scope.submit = function() {
            var now = new Date();
            var data = {accidentId : now.getTime(),
                iuserId : $scope.user.id,
                date : now.getFullYear() + "-" + (parseInt(now.getMonth()) + 1) + "-" + now.getDate() ,
                description : $scope.user.fname + " was crashed",
                geolocation : $scope.gmaplocation.latitude + " N," + $scope.gmaplocation.longitude + " E",
                damage : [],
                towingneeded : true,
                carreplacementneeded : true,
                injuries : $scope.injuried,
                thirdparty : []
            };

            backendSrv.reportAnAccident({accident : data})
                .then(
                function(data){
                    $scope.$apply(function() {
                            $scope.submitSucceded = true;
                            $scope.submitFalied = false;
                        }
                    );

                },
                //error
                function(data){
                    $scope.$apply(function() {
                            $scope.submitFalied = true;
                            $scope.submitSucceded = false;
                        }
                    );

                }
            );
        }

    }
);
