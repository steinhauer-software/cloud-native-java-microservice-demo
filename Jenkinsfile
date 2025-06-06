pipeline {
    agent any

    tools {
        jdk 'JDK 21'
    }

    environment {
        VERSION = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Docker mit Minikube verbinden
                    sh '''
                    eval $(minikube docker-env)
                    docker build -t product-service:${VERSION} .
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: "minikube-config", variable: 'KUBECONFIG')]) {
                    sh '''
                    sed -i "" "s/\\${VERSION}/${VERSION}/g" k8s/k8s-deployment.yml
                    kubectl --kubeconfig="${KUBECONFIG}" apply -f k8s/k8s-deployment.yml
                    '''
                }
            }
        }
    }
}
