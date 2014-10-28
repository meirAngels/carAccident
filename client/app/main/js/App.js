var reportingApp = angular.module('reportingApp', [
    'ngRoute',
    'reportingControllers'
]);

reportingApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'partials/login-page.html',
                controller: 'LoginCtrl'
            }).
            when('/selection', {
                templateUrl: 'partials/selection-page.html',
                controller: 'SelectionCtrl'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

var reportingControllers = angular.module('reportingControllers', []);