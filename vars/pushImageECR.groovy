def call(String imageName, String region = 'us-east-1') {
    echo "============================================"
    echo "Stage: PushImageECR"
    echo "============================================"

    script {
        def registry = imageName.split('/')[0]
        echo "Logging into ECR: ${registry}"

        withCredentials([
            string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
            string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')
        ]) {

            sh """
                AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
                AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
                aws ecr get-login-password --region ${region} \
                | docker login --username AWS --password-stdin ${registry}
            """
        }

        echo "Pushing image: ${imageName}"
        sh "docker push ${imageName}"
    }

    echo "Push image stage completed"
}
