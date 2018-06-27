package com.example.device.mqtt.receive;

import com.example.device.mqtt.send.MqttMsgPublisher;
import com.example.device.utils.SpringUtil;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class MsgArrivedTask implements Runnable {

    private Message<?> message;

    private MqttMsgPublisher mqttMsgPublisher;

    public MsgArrivedTask(Message<?> message){
        this.message = message;
        mqttMsgPublisher = (MqttMsgPublisher) SpringUtil.getBean(MqttMsgPublisher.class);
    }

    @Override
    public void run() {
        MessageHeaders headers = message.getHeaders();

        //获取报头和载荷
        String topic = String.valueOf(headers.get(MqttHeaders.RECEIVED_TOPIC));
        String data = String.valueOf(message.getPayload());

        System.out.println("received topic="+topic);
        System.out.println("received data="+data);

        if(data.contains("hello")){
            mqttMsgPublisher.sendMessage("world/cup/france","win the cup!!!");
        }

    }
}
