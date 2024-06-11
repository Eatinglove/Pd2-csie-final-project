package com.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscribeSample {
    //private static final int MAX_MESSAGES = 2; // ?置接收的最大消息?量
    //private static int receivedMessages = 0; // ??已接收的消息?量
   public static void main(String[] args) {
       String broker = "tcp://broker.emqx.io:1883";
       String topic = "mqtt/test";
       String username = "emqx";
       String password = "public";
       String clientid = "subscribe_client";
       int qos =0;

       try {
           MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
           // ?接??
           MqttConnectOptions options = new MqttConnectOptions();
           options.setUserName(username);
           options.setPassword(password.toCharArray());
           options.setConnectionTimeout(60);
           options.setKeepAliveInterval(5);
           // ?置回?
           client.setCallback(new MqttCallback() {

               public void connectionLost(Throwable cause) {
                   System.out.println("connectionLost: " + cause.getMessage());
              }

               public void messageArrived(String topic, MqttMessage message) {
                   System.out.println("topic: " + topic);
                   System.out.println("Qos: " + message.getQos());
                   System.out.println("message content: " + new String(message.getPayload()));
               
                
              }

               public void deliveryComplete(IMqttDeliveryToken token) {
                   System.out.println("deliveryComplete---------" + token.isComplete());
              }

          });
           client.connect(options);
           client.subscribe(topic, qos);
      } catch (Exception e) {
           e.printStackTrace();
      }
  }
}