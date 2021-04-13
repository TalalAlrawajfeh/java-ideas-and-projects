#!/bin/bash

if [ -f sales-manager-1.0-SNAPSHOT.war ]; then
    rm sales-manager-1.0-SNAPSHOT.war
fi

cd ..

mvn clean install

cp target/sales-manager-1.0-SNAPSHOT.war ./deploy/

cd deploy

docker build -t sales-manager-tomcat .

#docker run -p 8080:8080 \
#		--name "sales-tomcat" \ 
#		sales-manager-tomcat
