import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
 * Date: 2017-10-17
 */
public class MqttGenerator {
    private static final Logger log = LogManager.getLogger(MqttGenerator.class);

    public static void main(String[] args) throws MqttException, InterruptedException {
        new MqttGenerator().start();
    }

    private void start() throws MqttException {
        String broker = "tcp://localhost:1883";
        String clientId = "MqttGen";
        MemoryPersistence persistence = new MemoryPersistence();

        MqttClient client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        log.debug("Connecting to broker: " + broker);
        client.connect(connOpts);
        log.debug("Connected");

        MqttPublisher mqttPublisher = new MqttPublisher(client).startThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.debug("[MqttPublisher thread] Shutting down");
            mqttPublisher.stopThread();
            log.debug("[MqttPublisher thread] MqttPublisher complete");
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }));
        log.debug("[Main thread] MqttPublisher hook added");
    }

    private class MqttPublisher {
        /*
message: {"_type":"location","tid":"nx","acc":16,"batt":61,"conn":"m","lat":59.4313733,"lon":18.3259013,"tst":1508256136}
topic: owntracks/urban/nexus
message: {"_type":"location","tid":"nx","acc":22,"batt":50,"conn":"w","lat":59.434008,"lon":18.3240279,"t":"u","tst":1508266487}
topic: owntracks/urban/nexus
message: {"_type":"location","tid":"nx","acc":22,"batt":50,"conn":"w","lat":59.4339265,"lon":18.3240272,"tst":1508266496}
*/
        private List<String> list = Arrays.asList("{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":16,\"batt\":61,\"conn\":\"m\",\"lat\":59.4313733,\"lon\":18.3259013,\"tst\":1508256136}",
                "{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":22,\"batt\":50,\"conn\":\"w\",\"lat\":59.434008,\"lon\":18.3240279,\"t\":\"u\",\"tst\":1508266487}",
                "{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":22,\"batt\":50,\"conn\":\"w\",\"lat\":59.4339265,\"lon\":18.3240272,\"tst\":1508266496}");
        private final MqttClient client;

        private Thread thread = null;
        private String topic = "owntracks/urban/nexus";
        private int qos = 2;

        private MqttPublisher(MqttClient client) {
            this.client = client;
        }

        void stopThread() {

        }

        MqttPublisher startThread() {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        int i = ThreadLocalRandom.current().nextInt(0, 3);
                        String content = list.get(i);
                        log.debug("Publishing message: " + content);
                        MqttMessage message = new MqttMessage(content.getBytes());
                        message.setQos(qos);
                        client.publish(topic, message);
                        log.debug("Message published");
                        Thread.sleep(6000);
                    } catch (InterruptedException | MqttException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                log.debug("[Sample thread] Stopped");
            });
            thread.start();
            return this;
        }
    }
}