def call(){
        figlet 'CI'
        stage('buidAndTest') {
            STAGE=env.STAGE_NAME
            sh 'chmod a+x gradlew'
            sh './gradlew build'
        }
        stage('sonar') {
            STAGE=env.STAGE_NAME
            scannerHome = tool 'sonar-scanner'
            withSonarQubeEnv('Sonarqube-server') {
                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle2 -Dsonar.java.binaries=build"
            }
        }
        stage('runJar') {
            STAGE=env.STAGE_NAME
            sh 'nohup bash gradlew bootRun &'
            sleep(30)
        }
        stage('test') {
            STAGE=env.STAGE_NAME
            sh """curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"""
        }
        stage('nexusCI') {
            STAGE=env.STAGE_NAME
            nexusPublisher nexusInstanceId: 'nexus3', nexusRepositoryId: 'test-gradle', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'ejemplo-maven-feature-sonar', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
        figlet 'CD'
        stage('downloadNexus') {
            STAGE=env.STAGE_NAME
            sh 'curl -X GET -u admin:123456 http://localhost:8082/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
        }
        stage('runDownloadedJar') {
            STAGE=env.STAGE_NAME
            sh 'nohup java -jar DevOpsUsach2020-0.0.1.jar &'
            sleep(30)
        }
        stage('test') {
            STAGE=env.STAGE_NAME
            sh """curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"""
        }
        stage('nexusCD') {
            STAGE=env.STAGE_NAME
            nexusPublisher nexusInstanceId: 'nexus3', nexusRepositoryId: 'test-repo', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
        }
}

return this;