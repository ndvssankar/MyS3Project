#LogProcessor

# To Build the app
# go to app folder and run below command from terminal shell prompt
mvn clean install

# To run the app
# go to parent folder to app folder and run from terminal shell prompt
java -jar app/target/LogProcessor-1.0-SNAPSHOT.jar server app/config.dev.yaml

# To run the mongo db
# run below command
mongod -dbpath <path to mongo data>
# for example
mongod -dbpath /Users/sigtrak/mongodata

# To get to mongo shell run below command on shell prompt on terminal
mongo

# config file (below) has configuration params including path to test logs, json files generated etc
app/config.dev.yaml
