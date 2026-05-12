pipeline {
    agent any

    environment {
        REGISTRY = 'localhost:5000'
        IMAGE    = 'agenda'
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean bootJar'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/**/*.xml'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh """
                    docker build -t ${REGISTRY}/${IMAGE}:${BUILD_NUMBER} \
                                 -t ${REGISTRY}/${IMAGE}:latest .
                    docker push ${REGISTRY}/${IMAGE}:${BUILD_NUMBER}
                    docker push ${REGISTRY}/${IMAGE}:latest
                """
            }
        }
    }
}
