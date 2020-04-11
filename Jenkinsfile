
pipeline {

    agent any



    parameters {

        string(name: 'description',  description: 'Build description')

    }
    stages {

        stage('Build project') { // for display purposes

            steps {

                    withMaven(maven : 'apache-maven-3.6.3') {
                     sh 'mvn -Dmaven.test.skip=true clean package'
                    }


            }
        }

         stage('Upload jar to production server') { // for display purposes

                    steps {

                           script{
                                sh "printenv"
                           }
                          withCredentials([usernamePassword(credentialsId: 'axwaydmz', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) { {

                                  stage("Upload and start the Application") {

                                      sshPut remote: remote, from: 'abc.sh', into: '.'
                                      sshCommand remote: remote, command: 'pid=\$(lsof -i:8989 -t); kill -TERM \$pid || kill -KILL \$pid'
                                      sshCommand remote: remote, command: 'nohup ./mvnw spring-boot:run  &'

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
