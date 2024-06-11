package com.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;
//�u���o���\��
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

        // �w�q MQTT �N�z�B�D�D�B�Τ�W�B�K�X�B�Ȥ��ID�M�������e
        String topic = "hi";
        int qos = 0;

        try {
            Scanner scanner = new Scanner(System.in);//�ϥΪ̿�J�T��
            System.out.print("Enter message content: ");//�u�X���ϥΪ̿�J�L�Q�n���T��
            String content = scanner.nextLine(); //�S����n����
            scanner.close();

            // �Ыخ����ó]�m QoS�]�A�Ƚ�q���š^
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            // �o�G��������w���D�D
            client.publish(topic, message);
            //message.setRetained(true); // �]�m���O�d����
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

    public static void connect() {
        try {
            client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
            // MQTT ?��??
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            // �O�d??
            connOpts.setCleanSession(true);
            // ?�m�W???�A?���
            connOpts.setConnectionTimeout(timeout);
            // ?�m�߸�??�A?���A��ܪA?���C�j1.5*20��??�V��?��?�e�߸��P?��?�ݬO�_�b?
            connOpts.setKeepAliveInterval(keepAlive);
            // ?�m�^?
            client.setCallback(new OnMessageCallback());
            // �إ�?��
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
