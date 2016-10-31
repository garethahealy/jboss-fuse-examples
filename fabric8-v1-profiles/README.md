# Example of how to source control / deploy / handle profiles for fabric8
## Deploys to fabric8 mvn repo directly
mvn fabric8:deploy

## Create ZIP which can be imported into fabric8
mvn fabric8:zip

## Create a karaf file, which can be used to script the deployment
mvn fabric8:script
