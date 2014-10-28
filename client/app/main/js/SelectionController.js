reportingControllers.controller('SelectionCtrl',
    function ($scope, backendSrv) {
        $scope.greeting = backendSrv.foo();
    }
);
