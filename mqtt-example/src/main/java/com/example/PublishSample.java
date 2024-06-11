package com.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.Scanner;

public class PublishSample {

    public static void main(String[] args) { 

        // �w�q MQTT �N�z�B�D�D�B�Τ�W�B�K�X�B�Ȥ��ID�M�������e
        String broker = "tcp://broker.emqx.io:1883";
        String topic = "mqtt/test";
        String username = "publisher";
        String password = "public";
        String clientid = "publish_client";
        //String content = "456";//test msg
        int qos = 0;

        try {
            // �Ыؤ@�� MQTT �Ȥ�ݹ��
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());

            // �]�m�s���ﶵ
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username); // �]�m�Τ�W
            options.setPassword(password.toCharArray()); // �]�m�K�X
            options.setConnectionTimeout(60); // �]�m�s���W�ɮɶ��]��^
            options.setKeepAliveInterval(60); // �]�m�O���s�����j�]��^

            // �s���� MQTT �N�z
            client.connect(options);

            Scanner scanner = new Scanner(System.in);//�ϥΪ̿�J�T��
            System.out.print("Enter message content: ");//�u�X���ϥΪ̿�J�L�Q�n���T��
            String content = scanner.nextLine(); //�S����n����
            scanner.close();

            // �Ыخ����ó]�m QoS�]�A�Ƚ�q���š^
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            // �o�G��������w���D�D
            client.publish(topic, message);
            message.setRetained(true); // �]�m���O�d����
            System.out.println("Message published");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + content);

            // �_�}�s��
            client.disconnect();

            // �����Ȥ��
            client.close();
        } catch (MqttException e) {
            // ��o�� MQTT �ҥ~�ɡA�ߥX�B��ɲ��`
            throw new RuntimeException(e);
        }
    }
}
