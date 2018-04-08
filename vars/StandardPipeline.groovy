def call(body) {

        def config = [:]
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        node {
            // Clean workspace before doing anything
            deleteDir()

            try {
                stage ('Clone') {
                    checkout scm
                }
                stage ('Build') {
                     
                    bat "nuget restore ${config.projectName}.sln"
		            bat "\"${tool 'MSBuild'}\" ${config.projectName}.sln /p:Configuration=Release /p:Platform=\"Any CPU\" /p:ProductVersion=1.0.0.${env.BUILD_NUMBER}"
                }
                stage ('Tests') {
                    parallel 'static': {
                        
                    },
                    'unit': {
                        
                    },
                    'integration': {
                        
                    }
                }
                stage ('Deploy') {
                    
                }
            } catch (err) {
                currentBuild.result = 'FAILED'
                throw err
            }
        }
    }