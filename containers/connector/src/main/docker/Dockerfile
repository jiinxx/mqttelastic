FROM anapsix/alpine-java

RUN mkdir /opt/connector

COPY include/docker-entrypoint.sh /opt/connect
COPY maven/mqttelasticconnector-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/connector/connector.jar

ENTRYPOINT ["./docker-entrypoint.sh"]

CMD ["java", "-jar", "/opt/connector/connector.jar"]
