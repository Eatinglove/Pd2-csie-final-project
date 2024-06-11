package com.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;
//只有發布功能
public class PublishSample {

    private static final String hostUrl = "tcp://broker.emqx.io:1883";
    private static final String clientId = "publish_client";
    private static final String username = "publisher";
    private static final String password = "public";
    private static final int timeout = 60;
    private static final int keepAlive = 60;

    private static MqttClient client;

    public static void main(String[] args) {
        connect();

        // 定義 MQTT 代理、主題、用戶名、密碼、客戶端ID和消息內容
        String topic = "hi";
        int qos = 0;

        try {
            Scanner scanner = new Scanner(System.in);//使用者輸入訊息
            System.out.print("Enter message content: ");//彈出較使用者輸入他想要的訊息
            String content = scanner.nextLine(); //沒什麼好說的
            scanner.close();

            // 創建消息並設置 QoS（服務質量等級）
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            // 發佈消息到指定的主題
            client.publish(topic, message);
            //message.setRetained(true); // 設置為保留消息
            System.out.println("Message published");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + content);

            // 斷開連接
            client.disconnect();

            // 關閉客戶端
            client.close();
        } catch (MqttException e) {
            // 當發生 MQTT 例外時，拋出運行時異常
            throw new RuntimeException(e);
        }
    }

    public static void connect() {
        try {
            client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
            // MQTT ?接??
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            // 保留??
            connOpts.setCleanSession(true);
            // ?置超???，?位秒
            connOpts.setConnectionTimeout(timeout);
            // ?置心跳??，?位秒，表示服?器每隔1.5*20秒的??向客?端?送心跳判?客?端是否在?
            connOpts.setKeepAliveInterval(keepAlive);
            // ?置回?
            client.setCallback(new OnMessageCallback());
            // 建立?接
            client.connect(connOpts);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
            System.exit(0);
        }
    }

    private static class OnMessageCallback implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("Connection lost: " + cause.getMessage());
            System.exit(0);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Message arrived on topic " + topic + ": " + new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("Delivery complete: " + token.isComplete());
        }
    }
}
