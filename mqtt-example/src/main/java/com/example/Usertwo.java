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
            // ?��??
            MqttConnectOptions options = new MqttConnectOptions();
            // ?�m��?�W�M�K?
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);

            // ?��
            client.connect(options);
            // ?�خ����}?�m QoS
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            // ?������
            client.publish(topic, message);
            System.out.println("Message published");
            System.out.println("publish topic: " + topic);
            System.out.println("publish message content: " + content);
            // ???��
            client.disconnect();
            // ??��?��
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }


    public static void subscribe(){
        String broker = "tcp://broker.emqx.io:1883"; // MQTT �N�z���a�}
        String topic="userone";
        String username = "usertwo"; // �Τ�W
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
                options.setKeepAliveInterval(60); // �]�m�O�����ʶ��j
    
                // �]�m�q�\�^�ճB�z�{��
                client.setCallback(new MqttCallback() {
                    // �s���ᥢ�ɽե�
                    public void connectionLost(Throwable cause) {
                        System.out.println("connectionLost: " + cause.getMessage());
                        System.exit(0);
                }
    
                    // ����s�����ɽե�
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("receive topic: " + topic);
                        //System.out.println("rQos: " + message.getQos());
                        System.out.println("receive message content: " + new String(message.getPayload()));
                }
    
                    // ������ǻ������ɽե�
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
    
                // �s���� MQTT �N�z
                client.connect(options);
                // �q�\���w�D�D
                client.subscribe(topic,qos);
                //client.subscribe(topic1,qos);
                publish();
                // ���ݤ@�q�ɶ��A���������A�M���_�}�s��
                Thread.sleep(1000); // ���� 60 ����
                client.disconnect(); // �_�}�s��
                System.out.println("Disconnected from broker.");
        } catch (Exception e) {
                e.printStackTrace();
        }
        }
    }
