#!/usr/bin/groovy

def call(app, version){
    sh "oc new-app --name ${app}-${version} ${app}:${version} -l app=${app},svc=${app}-canary,hystrix.enabled=true"
    sh "oc patch dc/${app} -p '{\"spec\":{\"template\":{\"metadata\":{\"labels\":{\"svc\":\"${app}-canary\"}}}}}'"
    sh "oc patch svc/${app} -p '{\"spec\":{\"selector\":{\"svc\":\"${app}-canary\",\"app\": null, \"deploymentconfig\": null}, \"sessionAffinity\":\"None\"}}'"
}