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
            when('/handleClaims', {
                templateUrl: 'partials/claims-page.html',
                controller: 'ClaimsCtrl'
			}).
            when('/reporting', {
                templateUrl: 'partials/reporting-page.html',
                controller: 'ReportingCtrl'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);

reportingApp.directive('gmapsframe', function( $compile , locationSrv ) {

    function link( $scope, $element ) {

        locationSrv.getCurrentPosition().then(

            function(pos){
                var mapOptions = {
                    center: { lat: pos.coords.latitude, lng: pos.coords.longitude},
                    zoom: 15
                };

                var map = new google.maps.Map($element[0] , mapOptions);

                var myloc = new google.maps.Marker({
                    clickable: false,
                    icon: new google.maps.MarkerImage('//maps.gstatic.com/mapfiles/mobile/mobileimgs2.png',
                        new google.maps.Size(22,22),
                        new google.maps.Point(0,18),
                        new google.maps.Point(11,11)),
                    shadow: null,
                    zIndex: 999,
                    map: map
                });

                var me = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
                myloc.setPosition(me);
            },

            function(err){
                var tmpl;
                if(err){
                    tmpl  = '<p>' + 'Cannot fetch your location: ' + err.message + ' (error code ' + err.code + ')</p>';
                } else {
                    tmpl  = '<p>' + err + '</p>';
                }

                $element.append(tmpl);
            } );
    };

    return {
        restrict: 'A',
        link : link
    };
});

reportingApp.directive('bootstrapSwitch', [
    function() {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function(scope, element, attrs, ngModel) {
                element.bootstrapSwitch();

                if(attrs.onText) {
                    element.bootstrapSwitch('onText', attrs.onText);
                }
                if(attrs.offText) {
                    element.bootstrapSwitch('offText', attrs.offText);
                }

                element.on('switchChange.bootstrapSwitch', function(event, state) {
                    if (ngModel) {
                        scope.$apply(function() {
                            ngModel.$setViewValue(state);
                        });
                    }
                });

                scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                    if (newValue) {
                        element.bootstrapSwitch('state', true, true);
                    } else {
                        element.bootstrapSwitch('state', false, true);
                    }
                });
            }
        };
    }
]);

var reportingControllers = angular.module('reportingControllers', []);