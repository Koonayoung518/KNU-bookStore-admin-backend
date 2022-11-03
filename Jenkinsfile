pipeline{
		agent any

    options {
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        REPOSITORY_CREDENTIAL_ID = 'my-jenkins-github-key'
        REPOSITORY_URL = 'https://github.com/Koonayoung518/KNU-bookStore-admin-backend.git'
        TARGET_BRANCH = 'master'
				IMAGE_NAME = 'kny415/bookstore'
    }
	stages{
		stage('Init') {
			steps{
				echo 'clear'
				sh '''
                docker rm -f $IMAGE_NAME
                '''
				deleteDir()
				}
		    post {
		        success{
		            echo "Successfully Init"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}
	    stage('Clone') {
			steps{
				git url : "$REPOSITORY_URL",
				branch : "$TARGET_BRANCH",
				credentialsId : "$REPOSITORY_CREDENTIAL_ID"
				sh "ls -al"
				}

		    post {
		        success{
		            echo "Successfully Clone"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}

		stage('create jwt-secrets properties by aws parameter store') {
			steps{
					dir('src/main/resources/secret') {
				withAWSParameterStore(credentialsId: 'aws',
              				path: "	/bookstore/secret",
              				naming: 'basename',
              				regionName: 'ap-northeast-2') {
                               writeFile file: 'jwt-secrets.properties', text: "${env.JWT}"
				            echo "${env.JWT}"
           		   }
			}
			}
		    post {
		        success{
		            echo "Successfully create jwt-secrets properties by aws parameter store"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}

        stage('create api-key properties by aws parameter store') {
			steps{
				dir('src/main/resources/secret') {
				withAWSParameterStore(credentialsId: 'aws',
              				path: "/bookstore",
              				naming: 'basename',
              				regionName: 'ap-northeast-2') {
                               writeFile file: 'api-key.properties', text: "${env.NAVER}"
				echo '${env.NAVER}'
           		   }
			}
			}
		    post {
		        success{
		            echo "Successfully create api-key properties by aws parameter store"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}
        stage('create application-prod yml by aws parameter store') {
			steps{
					dir('src/main/resources') {
				withAWSParameterStore(credentialsId: 'aws',
              				path: "	/bookstore",
              				naming: 'basename',
              				regionName: 'ap-northeast-2') {
                               writeFile file: 'application-prod.yml', text: "${env.PROD}"
				            echo "${env.PROD}"
           		   }
			    }
			}

		    post {
		        success{
		            echo "Successfully create application-prod.yml by aws parameter store"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}

        stage('Backend Dockerizing') {
			steps{
				sh '''
				chmod +x mvnw

				./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=$IMAGE_NAME
				'''
				}

		    post {
		        success{
		            echo "Successfully Backend Dockerizing"
		        }
		        always{
		            echo "I tried..."
		        }
		        failure{
		            error 'This pipeline stops here...'
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}
	    stage('Deploy') {
			steps{
				sh 'docker run -e "SPRING_PROFILES_ACTIVE=prod" -m "700M" -d -p 8080:8080 -t $IMAGE_NAME'
				}

		    post {
		        success{
		            echo "Successfully Deploy"
		        }
		        always{
		            echo "I tried..."
		        }
		        cleanup{
		            echo "after all other post condition"
		        }
		    }
		}
	}
}