package com.example.device;

import com.example.device.mqtt.send.MqttMsgPublisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;

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
        while (true){

            publisher.sendMessage("world/cup/france","play games....");

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }




}
