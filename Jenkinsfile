#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v5.3.4').inside('-u root') {
        stage('check java') {
            sh "java -version"
        }

        stage('quality analysis') {
            withSonarQubeEnv('sonar-qube') {
            }
        }
    }
}
