#!/usr/bin/env groovy

pipeline {
    agent any
    stages {
        stage('clean') {
            steps {
                sh "java -version"
                sh "chmod +x mvnw"
                sh "./mvnw clean"
            }
        }
               stage('backend tests') {
                       steps {
                           script {
                               try {
                                   sh "./mvnw test"
                               } catch (err) {
                                   throw err
                               } finally {
                                   junit '**/target/surefire-reports/TEST-*.xml'
                               }
                          }
                      }
               }
        stage('packaging') {
            steps {
                sh "./mvnw verify -Pprod -DskipTests"
                archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                dir('target') { stash name: 'war', includes: 'postulacion.war' }
            }
        }
        stage('quality analysis') {
            withSonarQubeEnv('sonar-qube') {
            }
        }
//        stage('Pasaje a Testing') {
//            steps {
//                input "Desea copiar el war para instalar en testing?"
//            }
//        }
        //stage('Deploy - Testing') {
        //    steps {
        //        unstash 'war'
        //        sh "ls -lart /media/DeployJBoss/Testing"
        //        sh "cp postulacion.war /media/DeployJBoss/Testing"
        //        sh "ls -lart /media/DeployJBoss/Testing"
        //    }
        def dockerImage
        stage('build docker') {
        }

        stage('publish docker') {
            docker.withRegistry('https://registry.hub.docker.com', 'docker-login') {
                dockerImage.push 'latest'
            }
        }
    }
    post {
        success {
            mail to: 'sebastian.bogado@kimos.io',
                subject: "Build OK: ${currentBuild.fullDisplayName}",
                body: "El pipeline ${currentBuild.fullDisplayName} se completo exitosamente successfully en ${env.BUILD_URL}. " +
                    "Archivo diponible <b>postulacion.war</b> en //AMS12/DeployJBoss/Testing para deploy"
        }

        failure {
            mail to: 'sebastian.bogado@kimos.io',
                subject: "FALLÓ el Pipeline: ${currentBuild.fullDisplayName}",
                body: "Algo está mal con ${env.BUILD_URL}"
        }

    }
}


