/**
 * 
 */
package zz.maven.test;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author zhangle
 *
 */
public class MqttTest {

    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        try {
            IMqttClient subscriber = new MqttClient("tcp://mqtt.xskey.com.cn:8884", publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            subscriber.connect(options);

            //CountDownLatch receivedSignal = new CountDownLatch(10);
            subscriber.subscribe(EngineTemperatureSensor.TOPIC, (topic, msg) -> {
                byte[] payload = msg.getPayload();
                // ... payload handling omitted
//                receivedSignal.countDown();
                System.out.println(new String(payload));
            });
            //receivedSignal.await(1, TimeUnit.MINUTES);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
//        catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public static void publish() {
        String topic        = "topic";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://10.0.11.54:8884";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}

class EngineTemperatureSensor implements Callable<Void> {

    public static final String TOPIC = "topic";

    private IMqttClient client;
    private Random rnd = new Random();

    public EngineTemperatureSensor(IMqttClient client) {
        this.client = client;
    }

    @Override
    public Void call() throws Exception {

        if (!client.isConnected()) {
            return null;
        }

        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(TOPIC, msg);

        return null;
    }

    /**
     * This method simulates reading the engine temperature
     * 
     * @return
     */
    private MqttMessage readEngineTemp() {
        double temp = 80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temp).getBytes();
        MqttMessage msg = new MqttMessage(payload);
        return msg;
    }
}