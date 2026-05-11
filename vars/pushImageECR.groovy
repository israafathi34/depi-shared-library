#!/usr/bin/env groovy

/**
 * Shared Library Function: PushImageECR
 * Logs into AWS ECR, tags the image if needed, and pushes to ECR.
 *
 * @param imageName Full ECR image name (e.g. 123456789.dkr.ecr.us-east-1.amazonaws.com/ivolve-app:42)
 * @param region    AWS region (optional, defaults to us-east-1)
 */
stage('PushImage') {
    steps {
        script {
            echo "============================================"
            echo "Stage: PushImageECR"
            echo "============================================"

            withCredentials([
                string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
                string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')
            ]) {

                sh '''
                    echo "Configuring AWS credentials..."

                    aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                    aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                    aws configure set region us-east-1

                    echo "Logging into ECR..."

                    aws ecr get-login-password --region us-east-1 \
                    | docker login --username AWS --password-stdin 532334935385.dkr.ecr.us-east-1.amazonaws.com

                    echo "Pushing image..."

                    docker push 532334935385.dkr.ecr.us-east-1.amazonaws.com/depi:4
                    docker push 532334935385.dkr.ecr.us-east-1.amazonaws.com/depi:latest
                '''
            }
        }
    }
}
