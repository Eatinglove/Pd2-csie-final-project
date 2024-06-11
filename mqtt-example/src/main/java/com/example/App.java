package com.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {
    public static void main(String[] args) {
        String broker = "tcp://broker.emqx.io:1883";
        String username = "emqx";
        String password = "public";
        String clientid = "publish_client";

        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            client.connect(options);
            System.out.println("Connected to MQTT broker.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
