def remote = [:]
pipeline {

    agent any

    parameters {
        string(name: 'description', description: 'Build description')
    }

    stages {
        stage('Setup') {

            steps {
                withCredentials([file(credentialsId: 'runnersenv', variable: 'env')]) {

                    sh '''
                            cp $env runners.env
                           
                        '''
                }

            }
        }

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
                    script {
                        remote.name= 'vm'
                        remote.host = '208.67.130.105'
                        remote.user = username
                        remote.password = password
                        remote.knownHosts = '/home/axway/.ssh/known_hosts'
                        remote.port = 10022
                        sshPut remote: remote, from: 'target/runners.jar', into: '.'
                        sshPut remote: remote, from: 'runners.env', into: '.'
                        sshCommand remote: remote, command: 'pkill -f \'java -jar\'', failOnError:false
                        sshCommand remote: remote, command: 'pkill -f \'java -jar\'', failOnError:false
                        sshCommand remote: remote, command: "sh run.sh"
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
