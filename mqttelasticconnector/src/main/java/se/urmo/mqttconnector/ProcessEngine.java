package se.urmo.mqttconnector;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.elasticsearch.client.RestClient;

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
    private static final String ELASTIC_HOST = "localhost";
    private static final String MQTT_HOST = "localhost";
    private static final int ELASTIC_PORT = 9200;
    private static final int MQTT_PORT = 1883;
    private static ConnectionProcess process;

    private static ProcessEngine createProcessEngine() throws MqttException {
        String broker = String.format("tcp://%s:%s", MQTT_HOST, MQTT_PORT);
        String clientId = "MQTTConnectClientId";
        MemoryPersistence persistence = new MemoryPersistence();

        return new ProcessEngine() {
            ProcessEngine create() throws MqttException {
                MqttAsyncClient mqttClient = new MqttAsyncClient(broker, clientId, persistence);

                RestClient elasticClient = RestClient
                        .builder(new HttpHost(ELASTIC_HOST, ELASTIC_PORT, HttpHost.DEFAULT_SCHEME_NAME))
                        .build();

                process = ConnectionProcess
                        .builder(mqttClient, elasticClient)
                        .build();


                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    log.info("Shutting down");
                    process.stop();
                    log.info("MqttPublisher complete");
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
}
