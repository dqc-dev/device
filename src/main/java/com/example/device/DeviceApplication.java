package com.example.device;

import com.example.device.constants.MqttConstants;
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
        SpringApplication.run(DeviceApplication.class);
    }

//    @Bean
//    public MessageProducer inbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter(MqttConstants.MQTT_HOST,MqttConstants.MQTT_CLIENT_ID ,"dev/up/sta/00010010020001220161025001000001");
//        DirectChannel channel = new DirectChannel();
//        adapter.setOutputChannel();
//        return adapter;
//    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(MqttConstants.MQTT_HOST, MqttConstants.MQTT_CLIENT_ID, "dev/up/sta/00010010020001220161025001000001");
        adapter.setCompletionTimeout(5000);

        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                MessageHeaders headers = message.getHeaders();
                System.out.println(headers.get(MqttHeaders.RECEIVED_TOPIC));
                byte[] payload = (byte[]) message.getPayload();
                parse(payload[0]);
            }

        };
    }

    private void parse(byte firstByte){
        //bit0:开关状态, 0x1:开, 0x0:关
        int switchStatus = (firstByte << 7 & 0xFF) >>> 7;
        System.out.println("switchStatus="+switchStatus);

        //bit1~2:风速状态, 01:低挡, 10:中档, 11:高, 00：关闭状态。
        int windSpeed = (firstByte << 5 & 0xFF) >>> 6;
        System.out.println("windSpeed="+windSpeed);

        //bit3:锁定状态, 0x1:锁定, 0x0:不锁定;
        int lockStatus = (firstByte << 4 & 0xFF) >>> 7;
        System.out.println("lockStatus="+lockStatus);

        //bit4 : UV 模块状态， 0x1 表示杀菌， 0x0 表示空闲；
        int uvStatus = (firstByte << 3 & 0xFF) >>> 7;
        System.out.println("uvStatus="+uvStatus);

        //bit5~6 : 运行模式
        //飓风设备： 00:睡眠, 01:自动, 10:飓风， 11:保留
        //新风设备： 00:净化, 01:自动, 10:新风,  11:保留
        int runMode = (firstByte << 1 & 0xFF)>>> 6;
        System.out.println("runMode="+runMode);

        //bit7 : 童锁
        int childLock = (firstByte & 0xFF)>>> 7;
        System.out.println("childLock="+childLock);
    }

}
