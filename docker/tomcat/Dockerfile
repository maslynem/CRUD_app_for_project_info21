FROM tomcat:9.0.38-jdk8-openjdk

COPY ./war/info21.war /usr/local/tomcat/webapps/info-21.war

ENTRYPOINT ["catalina.sh", "jpda", "run"]