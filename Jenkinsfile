#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "echo $PATH"
        sh "java -version"
    }

    stage('clean') {
        //sh "chmod +x gradlew"
        sh "gradle clean --no-daemon --stacktrace"
    }


    stage('backend tests') {
        try {
            sh "gradle test -PnodeInstall --no-daemon --stacktrace"
        } catch(err) {
            throw err
        } finally {
            junit '**/build/**/TEST-*.xml'
        }
    }

    stage('packaging') {
        sh "gradle bootWar -x test -Pprod -PnodeInstall --no-daemon --stacktrace"
        archiveArtifacts artifacts: '**/build/libs/*.war', fingerprint: true
    }

    stage('quality analysis') {
        withSonarQubeEnv('sonar-qube') {
            sh "gradle sonarqube --no-daemon"
        }
    }

    def dockerImage
    stage('build docker') {
        sh "cp -R src/main/docker build/"
        sh "cp build/libs/*.war build/docker/"
        dockerImage = docker.build('kimosproject/monolith', 'build/docker')
    }

    stage('publish docker') {
        docker.withRegistry('https://registry.hub.docker.com', 'docker-login') {
            dockerImage.push 'latest'
        }
    }
}
