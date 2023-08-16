FROM jboss/wildfly:10.1.0.Final

# POSTGRES MODULE DRIVER
COPY ./modules/postgresql/ /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/
# STANDALONE CONF
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
# DEPLOYMENTS
COPY ./deployments/ /opt/jboss/wildfly/standalone/deployments/
# ADD USER
RUN /opt/jboss/wildfly/bin/add-user.sh docker docker --silent
# EXECUTION
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
