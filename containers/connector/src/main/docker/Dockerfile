FROM anapsix/alpine-java

ENV PATH /usr/share/connector/bin:$PATH

#RUN groupadd -g 1000 connector && adduser -u 1000 -g 1000 -d /usr/share/connector connector

WORKDIR /usr/share/connector

COPY include/docker-entrypoint.sh /
COPY include/bin /usr/share/connector/bin
COPY maven/mqttelasticconnector-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/share/connector/connector.jar

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["java", "-jar", "/opt/connector/connector.jar"]
