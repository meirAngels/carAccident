reportingControllers.controller('SelectionCtrl',
    function ($scope,  $location, backendSrv , userSrv) {

        $scope.showReportAccident = true;
        $scope.showHandleOpenClaims = false;
        $scope.showFollowUpOnCaseProgress = true;

        $scope.$on("roleForSelection", function(event, role) {

        })


        $scope.reportAccident = function(){
            $location.path("/reporting")
        }

        $scope.handleOpenClaims = function(){
            $location.path("/handleClaims")
        }

        $scope.followUpOnCaseProgress = function(){
            $location.path("/")
        }

        $scope.$on('$routeChangeSuccess', function (event) {
            $scope.showHandleOpenClaims = true;
            userSrv.whoAmI().then(
                function(data){
                    $scope.$apply(function() {
                            $scope.showHandleOpenClaims = data.isRepresentative;
                        }
                    );

                }
            );
        });
    }
);
