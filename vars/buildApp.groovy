#!/usr/bin/groovy

def call(project, app){
    projectSet(project)
    sh "oc new-build --binary --name=${app} -l app=${app} || echo 'Build exists'"
    sh "oc start-build ${app} --from-dir=. --follow"
    deployApp(app)
}