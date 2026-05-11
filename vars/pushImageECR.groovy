#!/usr/bin/env groovy

/**
 * Shared Library Function: PushImageECR
 * Logs into AWS ECR, tags the image if needed, and pushes to ECR.
 *
 * @param imageName Full ECR image name (e.g. 123456789.dkr.ecr.us-east-1.amazonaws.com/ivolve-app:42)
 * @param region    AWS region (optional, defaults to us-east-1)
 */
def call(String imageName, String region = 'us-east-1') {

    echo "============================================"
    echo "Stage: PushImageECR"
    echo "============================================"

    script {
        def registry = imageName.split('/')[0]
        echo "Logging into ECR: ${registry}"

        withCredentials([
            [$class: 'AmazonWebServicesCredentialsBinding',
             credentialsId: 'aws-credentials-id']
        ]) {

            sh """
                aws ecr get-login-password --region ${region} \
                | docker login --username AWS --password-stdin ${registry}
            """
        }

        echo "Pushing image: ${imageName}"
        sh "docker push ${imageName}"
    }

    echo "Push image stage completed"
}
