//Gruntfile
module.exports = function(grunt) {

    var SRC_BASE = './app';
    var DST_BASE = './public';
    var LIVERELOAD_PORT = 8989;

	//Initializing the configuration object
    grunt.initConfig( {});

	// Plugin loading
    grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-express');


    // Environment settings
    grunt.config('pkg', grunt.file.readJSON('package.json'));
    grunt.config('env',      grunt.option('env') || process.env.GRUNT_ENV || 'dev');
    grunt.config('compress', grunt.config('env') === 'prod' || grunt.config('env') === 'production');

    // Task configuration
    grunt.config('clean', [DST_BASE]);
    grunt.config('copy', {
                    main: {
                        src: SRC_BASE + '/main/index.html',
                        dest: DST_BASE + '/index.html'
                    },
                    glyphicons: {
                        cwd: 'bower_components/bootstrap/dist/fonts/',
                        src: '*',
                        dest: DST_BASE + '/fonts/',
                        flatten: true,
                        expand: true
                    },
                    bootstrap: {
                        cwd: 'bower_components/bootstrap/dist/',
                        src: '**/*',
                        dest: DST_BASE + '/assets/bootstrap/',
                        flatten: false,
                        expand: true
                    },
                    font_awesome: {
                        cwd: 'bower_components/font-awesome/fonts/',
                        src: '*',
                        dest: DST_BASE + '/fonts/',
                        flatten: true,
                        expand: true
                    },
                    images: {
                        cwd: SRC_BASE + '/assets/img/',
                        src: '*',
                        dest: DST_BASE + '/assets/img/',
                        flatten: true,
                        expand: true
                    },
                    partials_ng: {
                        cwd: SRC_BASE + '/main/partials/',
                        src: '*',
                        dest: DST_BASE + '/partials/',
                        flatten: true,
                        expand: true
                    }

                });
    grunt.config('concat', {
                    options: {
                        separator: ';'
                    },
                    js_frontend: {
                        src: [
//                                './bower_components/jquery/dist/jquery.js',
                                './bower_components/jquery/jquery.js',
                                './bower_components/typeahead.js/dist/typeahead.bundle.js',
                                './bower_components/angularjs/angular.min.js',
                                './bower_components/angular-route/angular-route.min.js',
                                './bower_components/q/q.js',
                                './bower_components/bootstrap-switch/dist/js/bootstrap-switch.min.js',

                                SRC_BASE + '/main/js/*'
                        ],
                        dest: DST_BASE + '/javascript/frontend.js'
                    }
                });
    grunt.config('less', {
                    development: {
                        options: {
                            compress: grunt.config('compress')
                        },
                        files: {
                            './public/styles/frontend.css': ['./app/main/frontend.main.less','./bower_components/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.css'],
                            './public/assets/fonts/font-awesome.css': ['./bower_components/font-awesome/less/font-awesome.less']
                        }
                    }
                });
    grunt.config('uglify', {
                    options: {
                        mangle: false	// Use if you want the names of your functions and variables unchanged
                    },
                    frontend: {
                        files: [{
                            src:  DST_BASE + '/javascript/frontend.js',
                            dest: DST_BASE + '/javascript/frontend.js'
                        }]
                    }
                });

//    grunt.config('express', {
//                    all: {
//                        options: {
//                            bases: ['./public'],
//                            port: LIVERELOAD_PORT,
//                            hostname: '0.0.0.0',
//                            livereload: true
//                        }
//                    },
//                    stylish: {
//                        options: {
//                            bases: ['./tmp/stylish-portfolio'],
//                            port: 9898,
//                            hostname: '0.0.0.0'
//                        }
//                    }
//                });

    grunt.config('watch', {
                    options: {
                        nospawn: true
                    },
                    less: {
                        files: ['app/**/*.less'],
                        tasks: ['less']
                    },
                    all: {
                        options: {
                            livereload: true
                        },
                        files: [
                            'app/**/*.less',
                            'app/**/*.html',
                            'app/**/*.css',
                            'app/**/*.js',
                            'app/**/*.{png,jpg,jpeg,gif,webp,svg}'
                        ],
                        tasks: ['build']
                    }
                });

	// Task definition
//	grunt.registerTask('default', ['build', 'express', 'watch']);
    grunt.registerTask('default', ['build', 'watch']);
    var build_tasks = ['less', 'concat', 'copy'];
    if (grunt.config('compress')) {
        build_tasks.push('uglify');
    }
    grunt.registerTask('build', build_tasks);
    grunt.registerTask('full', ['clean', 'build']);

};
