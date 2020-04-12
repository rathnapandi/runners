pipeline {

    agent any

    parameters {
        string(name: 'description', description: 'Build description')
    }

    stages {
        stage('Build project') { // for display purposes
            steps {
                withMaven(maven: 'apache-maven-3.6.3', jdk: 'jdk8') {
                    sh 'mvn -Dmaven.test.skip=true clean package'
                }
            }
        }

        stage('Upload and start the Application') { // for display purposes
            steps {
                script {
                    sh "printenv"
                }
                withCredentials([usernamePassword(credentialsId: 'axwaydmz', usernameVariable: 'username', passwordVariable: 'password')]) {
                    remote.host = '208.67.130.105'
                    remote.user = username
                    remote.password = password
                    script {
                        sshPut remote: remote, from: '${WORKSPACE}/target/runners.jar', into: '.'
                        sshCommand remote: remote, command: 'pid=\$(lsof -i:8080 -t); kill -TERM \$pid || kill -KILL \$pid'
                        sshCommand remote: remote, command: 'nohup java -jar runners.jar  &'
                    }

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
