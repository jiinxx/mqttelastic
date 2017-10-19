import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;

/**
 * Copyright (c) Ericsson AB, 2016.
 * <p/>
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 * <p/>
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * <p/>
 * User: eurbmod
 * Date: 2017-10-19
 */
public class ProcessEngine {
    private static final Logger log = LogManager.getLogger(ProcessEngine.class);
    private static MqttAsyncClient mqttClient;
    private static Process process;
    String broker = "tcp://localhost:1883";
    String clientId = "JavaAsyncSample";
    MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;

    private static ProcessEngine createProcessEngine() throws MqttException {

        return new ProcessEngine() {
            ProcessEngine create() throws MqttException {
                mqttClient = new MqttAsyncClient(broker, clientId, persistence);
                RestClient elasticClient = RestClient.builder(
                        new HttpHost("localhost", 9200, "http")).build();
                process = new Process(mqttClient, elasticClient);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    log.info("Shutting down");
                    process.stop();
                    log.info("MqttPublisher complete");
                    try {
                        mqttClient.disconnect();
                        elasticClient.close();
                    } catch (MqttException | IOException e) {
                        e.printStackTrace();
                    }
                }));

                return this;
            }
        }.create();
    }

    private void start() {
        process.start();
    }

    public static void main(String[] args) {
        try {
            ProcessEngine.createProcessEngine().start();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private class Process implements MqttCallback {

        private final MqttAsyncClient sourceClient;
        private final RestClient sinkClient;
        private final MqttConnectOptions connOpts;
        private Thread thread;

        public Process(MqttAsyncClient mqttClient, RestClient elasticClient) {
            this.sourceClient = mqttClient;
            this.sinkClient = elasticClient;

            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sourceClient.setCallback(this);
        }

        public void start() {
            thread = new Thread(() -> {
                try{
                    System.out.println("Connecting to broker: " + broker);
                    sourceClient.connect(connOpts);
                    System.out.println("Connected");
                    Thread.sleep(1000);
                    sourceClient.subscribe("#", qos);
                    System.out.println("Subscribed");
                }catch (InterruptedException ignored) {
                    log.warn("Thread interrupted");
                } catch (MqttException e){
                    log.error("Publish failed", e);
                }
            });
            thread.start();
        }

        public void stop() {

        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("topic: " + topic);
            System.out.println("message: " + new String(message.getPayload()));
            HttpEntity entity = new NStringEntity(new String(message.getPayload()), ContentType.APPLICATION_JSON);
            Response response = sinkClient.performRequest("PUT", "owntracks/urban/1", Collections.emptyMap(), entity);
            log.debug(EntityUtils.toString(response.getEntity()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }
}
