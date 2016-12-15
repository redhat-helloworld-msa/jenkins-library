#!/usr/bin/groovy

def call(app){
    sh "oc new-app ${app} -l app=${app},hystrix.enabled=true || echo 'Aplication already Exists'"
    sh "oc expose service ${app} || echo 'Service already exposed'"
    sh "oc patch dc/${app} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\",\"ports\":[{\"containerPort\": 8778,\"name\":\"jolokia\"}]}]}}}}'"
    sh "oc patch dc/${app} -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"${app}\",\"readinessProbe\":{\"httpGet\":{\"path\":\"/api/health\",\"port\":8080}}}]}}}}\'"
}