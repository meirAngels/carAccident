reportingControllers.controller('SelectionCtrl',
    function ($scope,  $location, backendSrv) {

        $scope.showReportAccident = true;
        $scope.showHandleOpenClaims = true;
        $scope.showFollowUpOnCaseProgress = true;

        $scope.$on("roleForSelection", function(event, role) {

        })


        $scope.reportAccident = function(){
            $location.path("/")
        }

        $scope.handleOpenClaims = function(){
            $location.path("/handleClaims")
        }

        $scope.followUpOnCaseProgress = function(){
            $location.path("/")
        }
    }
);
