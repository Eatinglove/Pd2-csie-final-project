package com.example;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscribeSample {
    //private static final int MAX_MESSAGES = 2; // �]�m�������̤j�����ƶq
    //private static int receivedMessages = 0; // �w�����������q

   public static void main(String[] args) {
       // MQTT �N�z�����]�m
       String broker = "tcp://broker.emqx.io:1883"; // MQTT �N�z���a�}
       String topic1 = "mqtt/test1"; // �q�\���D�D
       String topic2 = "mqtt/test2"; //test topic
       String username = "subscriber"; // �Τ�W
       String password = "public"; // �K�X
       String clientid = "subscribe_client"; // �Ȥ�� ID
       int qos = 0; // �]�m�q�\�� QoS ����

       try {
           // �Ы� MQTT �Ȥ��
           MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
           // �]�m�s���ﶵ
           MqttConnectOptions options = new MqttConnectOptions();
           options.setUserName(username); // �]�m�Τ�W
           options.setPassword(password.toCharArray()); // �]�m�K�X
           options.setConnectionTimeout(60); // �]�m�s���W�ɮɶ�
           options.setKeepAliveInterval(5); // �]�m�O�����ʶ��j

           // �]�m�q�\�^�ճB�z�{��
           client.setCallback(new MqttCallback() {
               // �s���ᥢ�ɽե�
               public void connectionLost(Throwable cause) {
                   System.out.println("connectionLost: " + cause.getMessage());
              }

               // ����s�����ɽե�
               public void messageArrived(String topic, MqttMessage message) {
                   System.out.println("topic: " + topic);
                   System.out.println("Qos: " + message.getQos());
                   System.out.println("message content: " + new String(message.getPayload()));

                    // �p�G����F topic2 ���T��
                    if (topic.equals(topic2)) {
                        // ���ܥΤ��J
                        System.out.println("Do you want to unsubscribe from topic2? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.nextLine().toLowerCase();
                        // �p�G��J�F y�A�h�����q�\ topic2 �D�D
                        if (input.equals("y")) {
                            try {
                                client.unsubscribe(topic2);
                                System.out.println("Unsubscribed from topic2.");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    }


              }

               // ������ǻ������ɽե�
               public void deliveryComplete(IMqttDeliveryToken token) {
                   System.out.println("deliveryComplete---------" + token.isComplete());
              }
          });

           // �s���� MQTT �N�z
           client.connect(options);
           // �q�\���w�D�D
           client.subscribe(topic1, qos);
           client.subscribe(topic2, qos);

           // ���ݤ@�q�ɶ��A���������A�M���_�}�s��
           Thread.sleep(30000); // ���� 30 ����
           client.disconnect(); // �_�}�s��
           System.out.println("Disconnected from broker.");
      } catch (Exception e) {
           e.printStackTrace();
      }
  }
}
