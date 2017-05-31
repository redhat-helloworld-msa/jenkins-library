#!/usr/bin/groovy

def call(origProject, project, app, tag){
    projectSet(project)
    sh "oc policy add-role-to-user system:image-puller system:serviceaccount:${project}:default -n ${origProject}"
    sh "oc tag ${origProject}/${app}:latest ${project}/${app}:${tag}"
    deployApp(project, app, tag)
}
