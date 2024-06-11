package com.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscribeSample {
    //private static final int MAX_MESSAGES = 2; // 設置接收的最大消息數量
    //private static int receivedMessages = 0; // 已接收的消息量

   public static void main(String[] args) {
       // MQTT 代理相關設置
       String broker = "tcp://broker.emqx.io:1883"; // MQTT 代理的地址
       String topic = "mqtt/test"; // 訂閱的主題
       String username = "subscriber"; // 用戶名
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
           options.setKeepAliveInterval(5); // 設置保持活動間隔

           // 設置訂閱回調處理程序
           client.setCallback(new MqttCallback() {
               // 連接丟失時調用
               public void connectionLost(Throwable cause) {
                   System.out.println("connectionLost: " + cause.getMessage());
              }

               // 當收到新消息時調用
               public void messageArrived(String topic, MqttMessage message) {
                   System.out.println("topic: " + topic);
                   System.out.println("Qos: " + message.getQos());
                   System.out.println("message content: " + new String(message.getPayload()));
              }

               // 當消息傳遞完成時調用
               public void deliveryComplete(IMqttDeliveryToken token) {
                   System.out.println("deliveryComplete---------" + token.isComplete());
              }
          });

           // 連接到 MQTT 代理
           client.connect(options);
           // 訂閱指定主題
           client.subscribe(topic, qos);

           // 等待一段時間，接收消息，然後斷開連接
           Thread.sleep(30000); // 等待 30 秒鐘
           client.disconnect(); // 斷開連接
           System.out.println("Disconnected from broker.");
      } catch (Exception e) {
           e.printStackTrace();
      }
  }
}
