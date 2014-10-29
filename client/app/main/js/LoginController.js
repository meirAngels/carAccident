reportingControllers.controller('LoginCtrl',
    function ($scope, $location, backendSrv) {

        $scope.username;
        $scope.password;

        $scope.signIn = function(){
            var username =  $scope.username;
            var password =  $scope.password;

            $location.path("/selection")
        }
    }
);
