#!/usr/bin/groovy

def call(project, app, version){
    sh "oc new-app -n ${project} --name ${app}-${version} ${app}:${version} -l app=${app},deploymentconfig=${app},hystrix.enabled=true"
    sh "oc set probe dc/${app} --readiness --get-url=http://:8080/api/health"
    sh "oc patch svc/${app} -n ${project} -p '{\"spec\":{\"selector\":{\"app\": \"${app}\", \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}' || echo 'Service ${app} already patched'"
}
