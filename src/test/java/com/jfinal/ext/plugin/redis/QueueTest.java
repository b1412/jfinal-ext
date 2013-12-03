package com.jfinal.ext.plugin.redis;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    @Before
    public void setUp() throws IOException {
        JedisPlugin jp = new JedisPlugin();
        jp.start();
    }

    @Test
    public void publish() {
        String queueName = "a";
        QueueProducer p = QueueProducer.create(queueName);
        for (int i = 0; i < 5; i++) {
            Bean bean = new Bean();
            bean.setAge(1 + i);
            bean.setName("kid" + i);
            System.out.println(p.publish(bean));
        }
    }

    @Test
    public void consume() {
        QueueConsumer c = QueueConsumer.create("a");
        Bean bean = c.consume();
        System.out.println(bean);
    }

    @Test
    public void consumeCallback() {
        QueueConsumer c = QueueConsumer.create("a");
        c.consume(new JedisMessage<Bean>() {
            int num = 0;

            @Override
            public void onMessage(Bean bean) {
                num++;
                if (num == 3) {
                    throw new RuntimeException("xx");
                }
                System.out.println(bean);
            }
        });
    }

    @Test
    public void produceAndConsume() {

    }
}
