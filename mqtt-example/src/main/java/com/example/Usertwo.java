package com.example;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Usertwo {

    public static void main(String[] args) {
        

       while(true){
        subscribe();
       }

    }
    public static void publish(){
        String broker = "tcp://broker.emqx.io:1883";
        String topic = "";
        String username = "usertwo";
        String password = "public";
        String clientid = "publish_client";
        String content="";
        //String content = "Hello MQTT";
        int qos = 0;
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("enter topic");
            topic = scanner.nextLine();
            if(topic.equals("-1")){
                System.exit(0);
            }
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
            System.out.println("publish topic: " + topic);
            System.out.println("publish message content: " + content);
            // ???接
            client.disconnect();
            // ??客?端
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }


    public static void subscribe(){
        String broker = "tcp://broker.emqx.io:1883"; // MQTT 代理的地址
        String topic="userone";
        String username = "usertwo"; // 用戶名
        String password = "public"; // 密碼
        String clientid = "subscribe_client"; // 客戶端 ID
        int qos = 0; // 設置訂閱的 QoS 等級
            try {
                // 創建 MQTT 客戶端
                MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
                // 設置連接選項
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(username); // 設置用戶名
                options.setPassword(password.toCharArray()); // 設置密碼
                options.setConnectionTimeout(60); // 設置連接超時時間
                options.setKeepAliveInterval(60); // 設置保持活動間隔
    
                // 設置訂閱回調處理程序
                client.setCallback(new MqttCallback() {
                    // 連接丟失時調用
                    public void connectionLost(Throwable cause) {
                        System.out.println("connectionLost: " + cause.getMessage());
                        System.exit(0);
                }
    
                    // 當收到新消息時調用
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("receive topic: " + topic);
                        //System.out.println("rQos: " + message.getQos());
                        System.out.println("receive message content: " + new String(message.getPayload()));
                }
    
                    // 當消息傳遞完成時調用
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
    
                // 連接到 MQTT 代理
                client.connect(options);
                // 訂閱指定主題
                client.subscribe(topic,qos);
                //client.subscribe(topic1,qos);
                publish();
                // 等待一段時間，接收消息，然後斷開連接
                Thread.sleep(1000); // 等待 60 秒鐘
                client.disconnect(); // 斷開連接
                System.out.println("Disconnected from broker.");
        } catch (Exception e) {
                e.printStackTrace();
        }
        }
    }
