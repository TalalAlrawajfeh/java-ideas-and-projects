#!/bin/bash

# start mysql docker
if [[ ! "$(docker ps -q -f name=cms-mysql)" ]]; then
    if [[ "$(docker ps -aq -f status=exited -f name=cms-mysql)" ]]; then
        docker rm cms-mysql
    fi
    docker pull mysql
    docker run --name cms-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql
fi

# wait for database server to wake up
while true
do
    curl -sSf http://localhost:3306 > /dev/null 2>&1
    RESULT=$?

    if [[ ${RESULT} -eq 0 ]]
    then
        break
    fi

    sleep 1
done

if [[ ! -d /opt/tomcat ]]
then
    sudo mkdir /opt/tomcat
    sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat

    wget http://www-us.apache.org/dist/tomcat/tomcat-9/v9.0.20/bin/apache-tomcat-9.0.20.tar.gz -P /tmp
    sudo tar xf /tmp/apache-tomcat-9*.tar.gz -C /opt/tomcat
    sudo ln -s /opt/tomcat/apache-tomcat-9.0.20 /opt/tomcat/latest
    sudo chown -RH tomcat: /opt/tomcat/latest
    sudo sh -c 'chmod +x /opt/tomcat/latest/bin/*.sh'

    sudo cp ./tomcat.service /etc/systemd/system/tomcat.service
    sudo systemctl daemon-reload
    sudo systemctl start tomcat
fi

sudo bash -c 'if [[ -d /opt/tomcat/latest/webapps/cms ]]; then rm -rf /opt/tomcat/latest/webapps/cms; fi'
sudo bash -c 'if [[ -d /opt/tomcat/latest/webapps/content ]]; then rm -rf /opt/tomcat/latest/webapps/content-0.0.1-SNAPSHOT; fi'
sudo bash -c 'if [[ -f /opt/tomcat/latest/webapps/cms.war ]]; then rm -rf /opt/tomcat/latest/webapps/cms.war; fi'
sudo bash -c 'if [[ -f /opt/tomcat/latest/webapps/content.war ]]; then rm -rf /opt/tomcat/latest/webapps/content.war; fi'

sudo cp ./cms/target/cms-*.war /opt/tomcat/latest/webapps/cms.war
sudo cp ./content/target/content-*.war /opt/tomcat/latest/webapps/content.war
sudo systemctl stop tomcat
sudo systemctl start tomcat

# wait for application to start
while true
do
    curl -sSf http://localhost:9090/cms/login > /dev/null 2>&1
    RESULT=$?

    if [[ ${RESULT} -eq 0 ]]
    then
        break
    fi

    sleep 1
done

google-chrome http://localhost:9090/cms/login

# mvn clean install; ./run.sh; sudo tail -f /opt/tomcat/latest/logs/localhost.2019-05-22.log
