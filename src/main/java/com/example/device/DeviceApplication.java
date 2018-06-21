package com.example.device;

import com.example.device.constants.MqttConstants;
import com.example.device.producer.MqttProducer;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.*;

@SpringBootApplication
@IntegrationComponentScan
public class DeviceApplication {


    public static void main(String[] args) {
//        SpringApplication.run(DeviceApplication.class);
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(DeviceApplication.class)
                        .web(false)
                        .run(args);
        MqttProducer.MyGateway gateway = context.getBean(MqttProducer.MyGateway.class);
        gateway.sendToMqtt("foo");
        String s = gateway.receiveMqtt();
        System.out.println(s);
    }




}
