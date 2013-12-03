package com.jfinal.ext.plugin.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class TopicTest {
    @Before
    public void setUp() throws IOException {
        JedisPlugin jp = new JedisPlugin();
        jp.start();
    }

    @Test
    public void lrange() {
        String key = "mylist";
        List<String> list = JedisKit.lrange(key, -1, -1);
        System.out.println(list);
    }

    @Test
    public void publishObject() {
        TopicPoducer p = new TopicPoducer("obj");
        final Bean bean = new Bean();
        bean.setName("name");
        bean.setAge(1);
        p.publish(bean);
    }

    @Test
    public void consumeObject() {
        TopicConsumer c = new TopicConsumer("a subscriber", "obj");
        Bean message = c.consume();
        System.out.println(message);
    }

    @Test
    public void clean() {
        TopicPoducer p = new TopicPoducer("foo");
        p.clean();
    }

    @Test
    public void publish() {
        TopicPoducer p = new TopicPoducer("foo");
        p.publish("hello world!");
    }

    @Test
    public void consume() {
        TopicConsumer c = new TopicConsumer("a subscriber", "foo");
        c.consume();
        TopicConsumer c1 = new TopicConsumer("a subscriber", "foo2");

        String message = c.consume();
        System.out.println(message);
    }

    @Test
    public void consumeCallback() {
        final TopicPoducer p = new TopicPoducer("foo");
        final Bean bean = new Bean();
        bean.setName("name");
        bean.setAge(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    p.publish(bean);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        TopicConsumer c = new TopicConsumer("a subscriber", "foo").interval(10000);
        c.consume(new JedisMessage<Bean>() {
            @Override
            public void onMessage(Bean message) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Test
    public void publishAndConsume() {
        TopicPoducer p = new TopicPoducer("foo");
        TopicConsumer c1 = new TopicConsumer("a subscriber", "foo");
        TopicConsumer c2 = new TopicConsumer("another subscriber", "foo");
        p.publish("hello world!");
        assertEquals("hello world!", c1.consume());
        assertEquals("hello world!", c2.consume());
    }

    @Test
    public void publishAndRead() {
        TopicPoducer p = new TopicPoducer("foo");
        p.clean();
        TopicConsumer c = new TopicConsumer("a subscriber", "foo");
        p.publish("hello world!");
        assertEquals("hello world!", c.read());
        assertEquals("hello world!", c.consume());
    }

    @Test
    public void unreadMessages() {
        TopicPoducer p = new TopicPoducer("foo");
        p.clean();
        TopicConsumer c = new TopicConsumer("a subscriber", "foo");

        assertEquals(0, c.unreadMessages());
        p.publish("hello world!");
        assertEquals(1, c.unreadMessages());
        p.publish("hello world!");
        assertEquals(2, c.unreadMessages());
        c.consume();
        assertEquals(1, c.unreadMessages());
    }

    @Test
    public void raceConditionsWhenPublishing() throws InterruptedException {
        TopicPoducer slow = new SlowProducer("foo");
        slow.clean();
        TopicConsumer c = new TopicConsumer("a subscriber", "foo");

        slow.publish("a");
        Thread t = new Thread(new Runnable() {
            public void run() {
                TopicPoducer fast = new TopicPoducer("foo");
                fast.clean();
                fast.publish("b");
            }
        });
        t.start();
        t.join();

        assertEquals("a", c.consume());
        assertEquals("b", c.consume());
    }

    @Test
    public void eraseOldMessages() {
        TopicPoducer p = new TopicPoducer("foo");

        TopicConsumer c = new TopicConsumer("a subscriber", "foo");
        while (c.consume() != null) {
            System.out.println(c.consume());
        }
        TopicConsumer nc = new TopicConsumer("new subscriber", "foo");
        p.publish("a");
        p.publish("b");
        assertEquals("a", c.consume());
        p.clean();
        assertEquals("b", c.consume());
        assertEquals("b", nc.consume());
        assertNull(c.consume());
        assertNull(nc.consume());
    }

    @Test
    public void expiredMessages() throws InterruptedException {
        TopicConsumer c = new SlowConsumer("a consumer", "foo", 2000L);
        TopicPoducer p = new TopicPoducer("foo");
        p.publish("un mensaje", 1);
        assertNull(c.consume());
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void firstMessageExpired() throws InterruptedException {
        TopicConsumer c = new SlowConsumer("a consumer", "foo", 2000L);
        TopicPoducer p = new TopicPoducer("foo");
        p.publish("1", 1);
        p.publish("2", 0);

        assertEquals("2", c.consume());
    }

    class SlowConsumer extends TopicConsumer {
        private long sleep;

        public SlowConsumer(String id, String topic) {
            this(id, topic, 500L);
        }

        public SlowConsumer(String id, String topic, long sleep) {
            super(id, topic);
            this.sleep = sleep;
        }

        @Override
        public String consume() {
            sleep(sleep);
            return super.consume();
        }

        @Override
        public void consume(JedisMessage callback) {
            sleep(sleep);
            super.consume(callback);
        }
    }

    class SlowProducer extends TopicPoducer {
        private long sleep;

        public SlowProducer(String topic) {
            this(topic, 500L);
        }

        public SlowProducer(String topic, long sleep) {
            super(topic);
            this.sleep = sleep;
        }

        protected Integer getNextMessageId() {
            Integer nextMessageId = super.getNextMessageId();
            sleep(sleep);
            return nextMessageId;
        }
    }

}
