package com.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.Scanner;

public class PublishSample {

    public static void main(String[] args) { 

        // 定義 MQTT 代理、主題、用戶名、密碼、客戶端ID和消息內容
        String broker = "tcp://broker.emqx.io:1883";
        String topic = "mqtt/test";
        String username = "publisher";
        String password = "public";
        String clientid = "publish_client";
        //String content = "456";//test msg
        int qos = 0;

        try {
            // 創建一個 MQTT 客戶端實例
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());

            // 設置連接選項
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username); // 設置用戶名
            options.setPassword(password.toCharArray()); // 設置密碼
            options.setConnectionTimeout(60); // 設置連接超時時間（秒）
            options.setKeepAliveInterval(60); // 設置保持連接間隔（秒）

            // 連接到 MQTT 代理
            client.connect(options);

            Scanner scanner = new Scanner(System.in);//使用者輸入訊息
            System.out.print("Enter message content: ");//彈出較使用者輸入他想要的訊息
            String content = scanner.nextLine(); //沒什麼好說的
            scanner.close();

            // 創建消息並設置 QoS（服務質量等級）
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            // 發佈消息到指定的主題
            client.publish(topic, message);
            message.setRetained(true); // 設置為保留消息
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
}
