#!/usr/bin/groovy

def call(project, app){
    sh "oc new-app ${app} -n ${project} -l app=${app},hystrix.enabled=true || echo 'Aplication already Exists'"
    sh "oc expose service ${app} -n ${project} || echo 'Service already exposed'"
    sh "oc set probe dc/${app} -n ${project} --readiness --get-url=http://:8080/api/health"
    sh "oc patch dc/${app} -n ${project} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\",\"ports\":[{\"containerPort\": 8778,\"name\":\"jolokia\"}]}]}}}}'"
}
