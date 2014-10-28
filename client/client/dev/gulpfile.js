var gulp = require("gulp"),
  help = require("gulp-help"),
  jshint = require('gulp-jshint'),
  reporter = require('jshint-stylish'),
  concat = require('gulp-concat'),
  ngAnnotate = require('gulp-ng-annotate'),
  uglify = require('gulp-uglify'),
  minifyCss = require('gulp-minify-css'),
  connect = require('gulp-connect'),
  rename = require('gulp-rename'),
  less = require('gulp-less'),
  openUrl = require("open"),
  preprocess = require('gulp-preprocess'),
  injectFiles = require("gulp-inject"),
  merge = require('merge-stream');

help(gulp);

gulp.task("test", "jshint files & run unit test", ["jshint"], function () {
});

gulp.task("release", "build and create a production ready assets", ["less-compile", "build-js" , "build-index-html"], function () {
});

gulp.task("serve-dev", "Build the app, starts a web Server with live-reload, serve raw files", ["serve"], function () {
  openUrl("http://localhost:" + 9090 + "/dist/index.dev.html");
});


gulp.task("serve-prod", "Build the app, starts a web Server with live-reload", ["serve"], function () {
  openUrl("http://localhost:" + 9090 + "/dist/index.prod.html");
});

gulp.task("serve", false, ["release", "run-server", "serve-watch"], function () {
});


gulp.task("jshint", false, function () {

  var filesToJshint = [
    '../app/**/*.js',
    './**/*.js',
    '!node_modules/**/*',
    '!../app/lib/**/*',
    '!/dist/**/*'
  ];

  return gulp.src(filesToJshint)
    .pipe(jshint('.jshintrc'))
    .pipe(jshint.reporter(reporter));

});

gulp.task("build-index-html", false, function () {

  var jsRawFiles = [
    '../app/**/*.js',
    '!../app/lib/**/*'];

  return merge(
    gulp.src("../app/index.html")
      .pipe(preprocess({
        context: {
          ENV: "prod"
        }
      }))
      .pipe(rename("index.prod.html"))
      .pipe(gulp.dest("../app/dist")),

    gulp.src("../app/index.html")
      .pipe(preprocess({
        context: {
          ENV: "dev"
        }
      }))
      .pipe(injectFiles(gulp.src(jsRawFiles, {read: false}), {relative: true, addPrefix: ".."}))
      .pipe(rename("index.dev.html"))
      .pipe(gulp.dest("../app/dist")));
});

gulp.task("run-server", false, function () {

  return connect.server({
    root: "../app",
    port: 9090,
    livereload: true
  });

});

gulp.task("reload-server", false, function () {
  return gulp.src([""]).pipe(connect.reload());
});

gulp.task('serve-watch', false, function () {
  var filesToWatch = [
    '../app/**/*.js',
    '../app/**/*.html',
    '../app/*.html',
    '../app/**/*.less',
    '!../app/dist',
    '../app/lib'
  ];

  gulp.watch(filesToWatch, ["release", "reload-server"]);

});

gulp.task("less-compile", false, function () {

  var lessFilesToCompile = [
    '../app/style/gvaot.less'
  ];

  return gulp.src(lessFilesToCompile)
    .pipe(less())
    .pipe(rename('gvaot.css'))
    .pipe(gulp.dest('../app/dist'))
    .pipe(rename('gvaot.min.css'))
    .pipe(minifyCss())
    .pipe(gulp.dest('../app/dist'));
});

gulp.task("build-js", false, function () {

  var jsFilesToInclude = [
    '../app/**/*.js',
    '!../app/dist/**/*',
    '!../app/lib/**/*'
  ];

  return gulp.src(jsFilesToInclude)
    .pipe(concat("gvaot.js"))
    .pipe(gulp.dest("../app/dist"))
    .pipe(ngAnnotate())
    .pipe(uglify())
    .pipe(rename("gvaot.min.js"))
    .pipe(gulp.dest("../app/dist"));
});
