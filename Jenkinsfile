#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
    stage('npm/gulp install') {
        sh "./gradlew npmInstall -PnodeInstall --no-daemon"
        sh "./gradlew gulp_test -PnodeInstall --no-daemon"
    }
	stage('build_Project') {
	    sh './gradlew clean build --info'
	}
}
