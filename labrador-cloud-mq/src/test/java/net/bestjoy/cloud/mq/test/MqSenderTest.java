package net.bestjoy.cloud.mq.test;

import org.junit.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MqSenderTest {

    @Test
    public void testSend() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setHost("39.108.8.230");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);

        admin.declareQueue(new Queue("myQueue"));

        AmqpTemplate template = new RabbitTemplate(connectionFactory);

        template.convertAndSend("myQueue", "foo");

        String foo = (String) template.receiveAndConvert("myQueue");

        System.out.println(foo);
    }
}
