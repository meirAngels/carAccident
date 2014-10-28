reportingApp.service('backendSrv',
    [
        function($http) {
            this.foo = function() {
                return 'boo';
            }
        }
    ]
);