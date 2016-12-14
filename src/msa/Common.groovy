#!/usr/bin/groovy
package msa

// Login and set the project
def projectSet(String project){
    //Use a credential called openshift-dev
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'openshift-dev', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
        sh "oc login --insecure-skip-tls-verify=true -u $env.USERNAME -p $env.PASSWORD $OPENSHIFT"
    }
    sh "oc new-project ${project} || echo 'Project exists'"
    sh "oc project ${project}"
}


// Creates a Build and triggers it
def buildApp(String project, String app){
    projectSet(project)
    sh "oc new-build --binary --name=${app} -l app=${app} || echo 'Build exists'"
    sh "oc start-build ${app} --from-dir=. --follow"
    deployApp(app)
}

// Tag the ImageStream from an original project to force a deployment
def promoteApp(String origProject, String project, String app){
    projectSet(project)
    sh "oc policy add-role-to-user system:image-puller system:serviceaccount:${project}:default -n ${origProject}"
    sh "oc tag ${origProject}/${app}:latest ${project}/${app}:latest"
    deployApp(app)
}

// Deploy the project based on a existing ImageStream
def deployApp(String app){
    sh "oc new-app ${app} -l app=${app},hystrix.enabled=true || echo 'Aplication already Exists'"
    sh "oc expose service ${app} || echo 'Service already exposed'"
    sh 'oc patch dc/${app} -p \'{"spec":{"template":{"spec":{"containers":[{"name":"${app}","ports":[{"containerPort": 8778,"name":"jolokia"}]}]}}}}\''
    sh 'oc patch dc/${app} -p \'{"spec":{"template":{"spec":{"containers":[{"name":"${app}","readinessProbe":{"httpGet":{"path":"/api/health","port":8080}}}]}}}}\''
}