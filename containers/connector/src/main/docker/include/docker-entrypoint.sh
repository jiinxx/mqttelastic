#!/usr/bin/env bash

set -e
# debug
#set -eux

function check_env_variables () {

   #if [[ ( -z "KAFKA_CONNECT" ) ]]; then
   #    echo "KAFKA_CONNECT must be configured"
   #    exit 1
   #fi

   #echo "KAFKA-STREAM: Running with 'KAFKA_CONNECT' $KAFKA_CONNECT"

}

if [ "$1" = 'connect' ]; then

  check_env_variables

#dub wait kafka 9092 20

#  java -Dkafka.connect=$KAFKA_CONNECT -jar /opt/kafka-stream/frauddetector.jar server /opt/kafka-stream/imei-fraud-detector.yml
  java -jar /opt/kafka-stream/mqttconnector.jar

else
    # Assume that user wants to run his own process,
    # for example a `bash` shell to explore this image
    exec "$@"
fi
