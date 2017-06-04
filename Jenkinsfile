#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
    stage('npm/gulp install') {
        sh "npm install"
        sh "./gradlew gulp_test -PnodeInstall --no-daemon"
    }
	stage('build_Project') {
	    sh './gradlew clean build --info'
	}

        sh "npm install --ignore-scripts"
	sh "bower install"

}
