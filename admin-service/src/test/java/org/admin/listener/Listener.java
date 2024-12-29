package org.admin.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Listener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "simple.queue"),
            exchange = @Exchange(name = "example.direct")
    ))
    public void simpleMQListener(String message) {
        System.out.println("[sout] 消费者收到了simple.queue的消息:" + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "example.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void MQListener1(String message) {
        System.out.println("[sout] 消费者1收到了direct.queue1的消息:" + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "example.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void MQListener2(String message) {
        System.out.println("[sout] 消费者2收到了direct.queue2的消息:" + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "object.queue"),
            exchange = @Exchange(name = "example.direct")
    ))
    public void MQListener(Map<String, Object> message) {
        System.out.println("[sout] 消费者收到了object.queue的消息:" + message);
    }

}
