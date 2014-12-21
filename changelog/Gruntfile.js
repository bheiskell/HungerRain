'use strict';

var xpath = require('xpath');
var dom = require('xmldom').DOMParser;


module.exports = function (grunt) {
    var xml = grunt.file.read(__dirname + '/../pom.xml');
    var doc = new dom().parseFromString(xml);
    var select = xpath.useNamespaces({"xmlns": "http://maven.apache.org/POM/4.0.0"});

    var version = select("/xmlns:project/xmlns:version/text()", doc).toString().split( /-/ )[0];
    var repository = select("/xmlns:project/xmlns:url/text()", doc).toString();

    require('load-grunt-tasks')(grunt);

    grunt.initConfig({
        changelog: {
            options: {
                version: version,
                repository: repository,
                dest: '../CHANGELOG.md'
            }
        },
    });

    grunt.registerTask('default', ['changelog']);
};
