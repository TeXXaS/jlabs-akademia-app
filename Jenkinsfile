#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
	stage('build_Project') {
	    sh './gradlew clean build --info'
	}
}
