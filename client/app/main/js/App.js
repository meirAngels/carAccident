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

reportingApp.factory('CameraService', function($window) {
    var hasUserMedia = function() {
        return !!getUserMedia();
    }

    var getUserMedia = function() {
        navigator.getUserMedia = ($window.navigator.getUserMedia ||
            $window.navigator.webkitGetUserMedia ||
            $window.navigator.mozGetUserMedia ||
            $window.navigator.msGetUserMedia);
        return navigator.getUserMedia;
    }

    return {
        hasUserMedia: hasUserMedia(),
        getUserMedia: getUserMedia
    }
})

reportingApp.directive('camera', function(CameraService) {
    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        scope: {},
        template: '<div class="camera"><video class="camera" autoplay="" /><div ng-transclude></div></div>',
        link: function(scope, ele, attrs) {
            var w = attrs.width || 320,
                h = attrs.height || 200;

            if (!CameraService.hasUserMedia) return;
            var userMedia = CameraService.getUserMedia(),
                videoElement = document.querySelector('video');
            var onSuccess = function(stream) {
                if (navigator.mozGetUserMedia) {
                    videoElement.mozSrcObject = stream;
                } else {
                    var vendorURL = window.URL || window.webkitURL;
                    videoElement.src = window.URL.createObjectURL(stream);
                }
                // Just to make sure it autoplays
                videoElement.play();
            }
// If there is an error
            var onFailure = function(err) {
                console.error(err);
            }
// Make the request for the media
            navigator.getUserMedia({
                video: {
                    mandatory: {
                        maxHeight: h,
                        maxWidth: w
                    }
                },
                audio: true
            }, onSuccess, onFailure);

            scope.w = w;
            scope.h = h;
        },
        controller: function($scope, $q, $timeout) {
            this.takeSnapshot = function() {
                var canvas  = document.querySelector('canvas'),
                    ctx     = canvas.getContext('2d'),
                    videoElement = document.querySelector('video'),
                    d       = $q.defer();

                canvas.width = $scope.w;
                canvas.height = $scope.h;

                $timeout(function() {
                    ctx.fillRect(0, 0, $scope.w, $scope.h);
                    ctx.drawImage(videoElement, 0, 0, $scope.w, $scope.h);
                    d.resolve(canvas.toDataURL());
                }, 0);
                return d.promise;
            }
        }
    }
});

reportingApp.directive('cameraControlSnapshot', function() {
    return {
        restrict: 'EA',
        require: '^camera',
        scope: true,
        template: '<a class="btn btn-info" ng-click="takeSnapshot()">Take snapshot</a>',
        link: function(scope, ele, attrs, cameraCtrl) {
            scope.takeSnapshot = function() {
                cameraCtrl.takeSnapshot()
                    .then(function(image) {
                        // data image here
                    });
            }
        }
    }
})

reportingApp.directive('gmapsframe', function( $compile , locationSrv ) {

    function link( $scope, $element ) {

        locationSrv.getCurrentPosition().then(

            function(pos){
                $scope.gmaplocation = pos.coords;
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

reportingControllers.controller('CameraController', function($scope, CameraService) {
    $scope.hasUserMedia = CameraService.hasUserMedia;
})