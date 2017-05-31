#!/usr/bin/groovy

def call(project) {
    sh "oc new-project ${project} || echo 'Project exists'"
    sh "oc project ${project}"
    sh "oc policy add-role-to-user admin developer -n ${project}"
}
