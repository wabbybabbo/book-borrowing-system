package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void simpleQueueTest() {
        //队列名称
        String queueName = "simple.queue";
        //消息
        String message = "hello,spring amqp!";
        //发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void batchSendTest() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            //发送消息
            rabbitTemplate.convertAndSend("simple.queue", "msg_" + i);
//            rabbitTemplate.convertAndSend("hello.queue", "msg_"+i);
            Thread.sleep(20);
        }
    }

    @Test
    public void send2FanoutExchangeTest() {
        //发送消息给 amq.fanout 交换机
        rabbitTemplate.convertAndSend("amq.fanout", null, "msg");
    }

    @Test
    public void send2DirectExchangeTest() {
//        //发送消息给 amq.direct 交换机
//        rabbitTemplate.convertAndSend("amq.direct","red", "msg_red");
//        rabbitTemplate.convertAndSend("amq.direct","blue", "msg_blue");
//        rabbitTemplate.convertAndSend("amq.direct","yellow", "msg_yellow");
        //发送消息给 example.direct 交换机
        rabbitTemplate.convertAndSend("example.direct", "red", "msg_red");
        rabbitTemplate.convertAndSend("example.direct", "blue", "msg_blue");
        rabbitTemplate.convertAndSend("example.direct", "yellow", "msg_yellow");
    }

    @Test
    public void send2TopicExchangeTest() {
        //发送消息给 amq.topic 交换机
//        rabbitTemplate.convertAndSend("amq.topic","International.news", "msg_news");
        rabbitTemplate.convertAndSend("amq.topic", "cn.city", "msg_city");
    }

    @Test
    public void sendObj2DirectExchangeTest() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("username", "wabbybabbo");
        msg.put("password", "123456");
        //发送消息给 example.direct 交换机
        rabbitTemplate.convertAndSend("example.direct", null, msg);
    }

}
