pipeline {
    agent Agent1
    stages {
	    stage('Deploy-Docker') {
			steps {
              sh 'ansible-playbook --inventory /tmp/inv $WORKSPACE/docker.yml --extra-vars "env=qa"'
			}
		}	
        stage('build & run docker image') {
	        steps {  
                    sh script: 'cd  $WORKSPACE'
                    sh script: 'docker build --file dfile --tag docker.io/arindam1987/Webapp:$BUILD_NUMBER .'
			}
			post {
               failure {
	               sh script: 'docker rm -f $(docker ps -q -f status=running -f ancestor=arindam1987/webapp:$BUILD_NUMBER)'
               }
           }
        }

    }
}
