package com.example.device.producer;

import com.example.device.constants.MqttConstants;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class MqttProducer {

    @Autowired
    private MqttPahoMessageHandler mqttPahoMessageHandler;

    public void sendMessage(String topic,String payload){
        Message<String> message = MessageBuilder.withPayload(payload).setHeader(MqttHeaders.TOPIC, topic).build();
        mqttPahoMessageHandler.handleMessage(message);
    }

}
