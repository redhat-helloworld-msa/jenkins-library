#!/usr/bin/groovy

def call(project, app, tag){
    sh "oc new-app --name=${app} -n ${project} -l app=${app},hystrix.enabled=true --image-stream=${project}/${app}:${tag} || echo 'Aplication already Exists'"
    sh "oc expose service ${app} -n ${project} || echo 'Service already exposed'"
    sh "oc set probe dc/${app} -n ${project} --readiness --get-url=http://:8080/api/health"
}
