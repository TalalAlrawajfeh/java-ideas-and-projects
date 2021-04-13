FROM tomcat:8-jre8

LABEL maintainer "talalalrawajfeh@gmail.com"

EXPOSE 8080

COPY sales-manager-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
RUN chmod a+r $CATALINA_HOME/webapps/sales-manager-1.0-SNAPSHOT.war
