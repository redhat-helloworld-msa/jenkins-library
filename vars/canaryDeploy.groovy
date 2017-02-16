#!/usr/bin/groovy

def call(project, app, version){
    sh "oc new-app -n ${project} --name ${app}-${version} ${app}:${version} -l app=${app},svc=${app}-canary,hystrix.enabled=true"
    sh "oc patch dc/${app} -n ${project} -p '{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"svc\":\"${app}-canary\"}}}}}'"
    sh "oc patch svc/${app} -n ${project} -p '{\"spec\":{\"selector\":{\"svc\":\"${app}-canary\",\"app\": null, \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}'"
}
