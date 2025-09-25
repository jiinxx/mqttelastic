package se.urmo.mqttconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.urmo.mqttconnector.model.ElasticLocation;
import se.urmo.mqttconnector.model.MQTTLocation;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;


public class ConnectionProcess implements MqttCallback {
    private static final Logger log = LogManager.getLogger(ConnectionProcess.class);
    public static final String ENDPOINT = "owntracks/location";
    public static final String TOPIC_FILTER = "owntracks/#";
    public static final String POST = "POST";
    private int qos = 2;

    private final MqttAsyncClient sourceClient;
    private final RestClient sinkClient;
    private MqttConnectOptions connOpts;
    private Thread thread;

    public ConnectionProcess(MqttAsyncClient mqttClient, RestClient elasticClient) {
        this.sourceClient = mqttClient;
        this.sinkClient = elasticClient;
    }

    public void start() {
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sourceClient.setCallback(this);

        thread = new Thread(() -> {
            try {
                sourceClient.connect(connOpts);
                log.info("Connected");
                Thread.sleep(1000);
                sourceClient.subscribe(TOPIC_FILTER, qos);
                log.info("Subscribed");
            } catch (InterruptedException ignored) {
                log.warn("Thread interrupted");
            } catch (MqttException e) {
                log.error("Publish failed", e);
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            log.info("Closeing resources..");
            sourceClient.disconnect();
            sinkClient.close();
            log.info("done.");
        } catch (IOException | MqttException e) {
            log.error("Failed to close resource", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("Message arrived (topic, message): ({}, {})  ",topic , new String(message.getPayload()));
        String[] topicArray = topic.split("/", 3);
        ObjectMapper objectMapper = new ObjectMapper();
        ElasticLocation e = ElasticLocation.from(objectMapper.readValue(message.getPayload(), MQTTLocation.class));
        e.setUid(topicArray[1]);
        e.setType(topicArray[1]);
        HttpEntity entity = new NStringEntity(objectMapper.writeValueAsString(e), ContentType.APPLICATION_JSON);
        this.sinkClient.performRequest(POST, ENDPOINT, Collections.emptyMap(), entity);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public static ConnectionProcessBuilder builder(MqttAsyncClient mqttClient, RestClient elasticClient) {
        return new ConnectionProcessBuilder()
                .withMQTTClient(mqttClient)
                .withElasticClient(elasticClient);
    }
    public static class ConnectionProcessBuilder{

        private RestClient elasticClient;
        private MqttAsyncClient mqttClient;

        private ConnectionProcessBuilder withMQTTClient(final MqttAsyncClient mqttClient) {
            Objects.requireNonNull(mqttClient, "MQTTClient can not be null");
            this.mqttClient = mqttClient;
            return this;
        }

        private ConnectionProcessBuilder withElasticClient(final RestClient elasticClient) {
            Objects.requireNonNull(elasticClient, "ElasticClient can not be null");
            this.elasticClient = elasticClient;
            return this;
        }

        public ConnectionProcess build() {
            return new ConnectionProcess(mqttClient, elasticClient);
        }
    }
}
