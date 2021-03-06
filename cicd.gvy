pipeline {
    agent any
    stages {
	    stage('Deploy-Docker') {
			steps {
              sh 'ansible-playbook --inventory $WORKSPACE/inv $WORKSPACE/docker.yml --extra-vars "env=qa"'
			}
		}	
        stage('build & run docker image') {
	        steps {  
                    	sh script: 'cd  $WORKSPACE'
                    	sh script: 'docker build --file dfile --tag docker.io/arindam1987/webapp:$BUILD_NUMBER .'
			sh script: 'docker run -d -P arindam1987/webapp:$BUILD_NUMBER'
			}
			post {
               failure {
	               sh script: 'docker rm -f $(docker ps -q -f status=running -f ancestor=arindam1987/webapp:$BUILD_NUMBER)'
               }
           }
        }

    }
}
