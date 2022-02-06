pipeline {
  
        agent any

        environment {
            STAGE = ''
            PIPELINE=''
        }

        parameters {
                choice(name: 'buildTool', choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción')
        }

        stages{
          stage('Pipeline'){
            steps{
              script{
                println 'Pipeline'
                  if (params.buildTool == "gradle") {
                      def ejecucion = load 'gradle.groovy'
                      ejecucion.call()
                  } else {
                      maven()
                  }
              }
            }
          }
        }

        post {
          success {
            slackSend color: 'good', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: Ok]"
          }
          failure {
            slackSend color: 'danger', message: "[Grupo6][${PIPELINE}][Rama: ${GIT_BRANCH}][Stage: ${STAGE}][Resultado: No Ok]"
            error "Ejecución fallida en stage ${STAGE}"
          }
        }
}
