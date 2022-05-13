pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                git branch: 'master', url: 'https://github.com/NihBertolo/trabalho-5-INF335.git'
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('JaCoCo Coverage') {
            steps {
                sh 'mvn clean install'
                jacoco(
                    execPattern:'target/**/*.exec',
                    classPattern:'target/classes',
                    sourcePattern:'src/main.java',
                    exclusionPattern:'src/test*',
                    skipCopyOfSrcFiles:false,
                    changeBuildStatus:true,
                    minimumInstructionCoverage:'100', maximumInstructionCoverage: '100',//Bytecode instruction coverage
                    minimumBranchCoverage:'100', maximumBranchCoverage: '100',//Branch coverage
                    buildOverBuild:true,
                )
            }
        }
    }
}
