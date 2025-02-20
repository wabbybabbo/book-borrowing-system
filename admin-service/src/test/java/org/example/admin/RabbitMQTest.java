package org.example.admin;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    public void test() {
        System.out.println("[sout] test");
    }

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
        msg.put("title", "消息通知标题");
        msg.put("content", "消息通知内容");
        //发送消息给 example.direct 交换机
        rabbitTemplate.convertAndSend("example.direct", "test", msg);
    }

    @Test
    public void createMQ() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        //声明消息接收队列
        Queue queue = new Queue("userId");
        //声明交换机 Topic
        TopicExchange topicExchange = new TopicExchange("amq.topic");
        //声明绑定
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("userId");

        //创建队列
        rabbitAdmin.declareQueue(queue);
        //创建交换机
        rabbitAdmin.declareExchange(topicExchange);
        //创建绑定关系
        rabbitAdmin.declareBinding(binding);
    }

    @Test
    public void deleteMQ() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);

        //删除队列
        rabbitAdmin.deleteQueue("userId");
        //删除交换机
        rabbitAdmin.deleteExchange("amq.topic");
    }

}
