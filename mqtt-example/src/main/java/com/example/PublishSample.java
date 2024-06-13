package com.example;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublishSample {

    public static void main(String[] args) {

        String broker = "tcp://broker.emqx.io:1883";
        String topic = "";
        String username = "emqx";
        String password = "public";
        String clientid = "publish_client";
        String content="";
        //String content = "Hello MQTT";
        int qos = 0;
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("enter topic");
            topic = scanner.nextLine();
            System.out.println(("enter content"));
            content = scanner.nextLine();
        }catch(Exception e){
            System.out.println("failed");
        }
        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            // ?接??
            MqttConnectOptions options = new MqttConnectOptions();
            // ?置用?名和密?
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);

            // ?接
            client.connect(options);
            // ?建消息并?置 QoS
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            // ?布消息
            client.publish(topic, message);
            System.out.println("Message published");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + content);
            // ???接
            client.disconnect();
            // ??客?端
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
    }
