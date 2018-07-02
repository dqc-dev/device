package com.example.device;

import com.example.device.constants.MqttConstants;
import com.example.device.enums.device.DeviceConnectEnum;
import com.example.device.mqtt.send.MqttMsgPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;

@SpringBootApplication
@IntegrationComponentScan
public class DeviceApplication {


    public static void main(String[] args) {
//        SpringApplication.run(DeviceApplication.class);
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(DeviceApplication.class)
                        .web(false)
                        .run(args);
        MqttMsgPublisher publisher = context.getBean(MqttMsgPublisher.class);

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(DeviceConnectEnum.CONNECT.getValue());


        byte[] payload = new byte[buffer.writerIndex()];

        buffer.readBytes(payload);

        publisher.sendMessage(MqttConstants.MQTT_TOPIC_DEV_UP_CONN+"0001002", payload);



//        while (true){
//
//            publisher.sendMessage("world/cup/france","play games....");
//
//            try {
//                Thread.sleep(30000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


    }




}
