import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import java.util.Arrays;

import static org.apache.commons.codec.CharEncoding.UTF_8;

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
public class TestMQTT {

    @Test
    public void test1() throws Exception {
        MqttClient client = new MqttClient(
                "tcp://localhost:1883", //URI
                MqttClient.generateClientId(), //ClientId
                new MemoryPersistence()); //Persistence
        client.connect();
        client.publish(
                "topic", // topic
                "payload".getBytes(UTF_8), // payload
                2, // QoS
                false); // retained?
    }

    @Test
    public void test() throws Exception {
        MqttClient client = new MqttClient(
                "tcp://localhost:1883", //URI
                MqttClient.generateClientId(), //ClientId
                new MemoryPersistence()); //Persistence

        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) { //Called when the client lost the connection to the broker
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete
            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setKeepAliveInterval(10);
        client.connect(options);
        client.subscribe("#", 1);
    }
}
