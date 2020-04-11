pipeline {

    agent any

    parameters {
        string(name: 'description', description: 'Build description')
    }

    stages {
        stage('Build project') { // for display purposes
            steps {
                withMaven{
                    sh 'mvn -Dmaven.test.skip=true clean package'
                }
            }
        }

        stage('Upload and start the Application') { // for display purposes
            steps {
                script {
                    sh "printenv"
                }
                withCredentials([usernamePassword(credentialsId: 'axwaydmz')]) {
                    sshPut remote: remote, from: 'abc.sh', into: '.'
                    sshCommand remote: remote, command: 'pid=\$(lsof -i:8989 -t); kill -TERM \$pid || kill -KILL \$pid'
                    sshCommand remote: remote, command: 'nohup ./mvnw spring-boot:run  &'

                }

            }
        }

    }
    post {
        cleanup {
            deleteDir()
        }
    }
}
