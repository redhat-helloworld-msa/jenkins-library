#!/usr/bin/groovy

def call(project, app){
    sh "oc new-app ${app} -n ${project} -l app=${app},hystrix.enabled=true || echo 'Aplication already Exists'"
    sh "oc expose service ${app} -n ${project} || echo 'Service already exposed'"
    sh "oc patch dc/${app} -n ${project} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\",\"ports\":[{\"containerPort\": 8778,\"name\":\"jolokia\"}]}]}}}}'"
    sh "oc patch dc/${app} -n ${project} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\",\"readinessProbe\":{\"httpGet\":{\"path\":\"/api/health\",\"port\":8080}}}]}}}}\'"
}
