#!/usr/bin/groovy

def call(project) {
    //Use a credential called openshift-dev
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'openshift-dev', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
        sh "oc login --insecure-skip-tls-verify=true -u $env.USERNAME -p $env.PASSWORD $OPENSHIFT"
    }
    sh "oc new-project ${project} || echo 'Project exists'"
    sh "oc project ${project}"
}