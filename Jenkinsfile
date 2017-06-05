#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
    stage('npm/gulp install') {
        sh "npm install"
        sh "bower install"
        sh "./gradlew gulp_test -PnodeInstall --no-daemon"
    }
	stage('build') {
	    sh './gradlew clean build --info'
	}

	stage('deploy') {
		def destinationWarFile = "app-${env.BUILD_NUMBER}.war"
		def versionLabel = "app#${env.BUILD_NUMBER}"
		def description = "${env.BUILD_URL}"

		sh """\
		  aws s3 cp build/libs/app-0.0.1-SNAPSHOT.war s3://texxas-jenkins-backup/builds/$destinationWarFile
		  aws elasticbeanstalk create-application-version --source-bundle S3Bucket=texxas-jenkins-backup,S3Key=builds/$destinationWarFile --application-name app --version-label $versionLabel --description \\\"$description\\\"
		  aws elasticbeanstalk update-environment --environment-name game-of-life-prod --application-name app --version-label $versionLabel --description \\\"$description\\\"
		"""
	}
}
