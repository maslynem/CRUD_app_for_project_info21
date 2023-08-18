FROM tomcat:8.0-jre8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./war/info21.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]