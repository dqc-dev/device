package com.example.device;

import com.example.device.producer.MqttProducer;
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
        MqttProducer producer = context.getBean(MqttProducer.class);
        while (true){

            producer.sendMessage("world/cup/france","win");

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }




}
