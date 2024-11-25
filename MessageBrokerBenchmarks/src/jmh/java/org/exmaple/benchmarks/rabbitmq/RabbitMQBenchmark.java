package org.exmaple.benchmarks.rabbitmq;

import com.rabbitmq.client.*;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class RabbitMQBenchmark {

    private static final String QUEUE_NAME = "test-queue";
    private static final String EXCHANGE_NAME = "test-exchange";

    private ConnectionFactory factory;
    private Connection connection;
    private List<Channel> producerChannels;
    private List<Channel> consumerChannels;

    @Param({"1:1", "3:1", "1:3", "3:3", "10:10"})
    private String config;

    private AtomicLong messagesProduced = new AtomicLong(0);
    private AtomicLong messagesConsumed = new AtomicLong(0);
    private AtomicLong totalProducerLatency = new AtomicLong(0);
    private AtomicLong totalDeliveryLatency = new AtomicLong(0);
    private AtomicLong totalConsumerProcessingTime = new AtomicLong(0);

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        connection = factory.newConnection();

        String[] parts = config.split(":");
        int producerCount = Integer.parseInt(parts[0]);
        int consumerCount = Integer.parseInt(parts[1]);

        producerChannels = createChannels(producerCount);
        consumerChannels = createChannels(consumerCount);

        producerChannels.getFirst().exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
        producerChannels.getFirst().queueDeclare(QUEUE_NAME, true, false, false, null);
        producerChannels.getFirst().queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
    }

    private List<Channel> createChannels(int count) throws Exception {
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            channels.add(connection.createChannel());
        }
        return channels;
    }

    @Benchmark
    @Threads(10)
    public void Messages() throws IOException {
        Channel producerChannel = producerChannels.get(0);
        long producerStartTime = System.nanoTime();
        String message = String.valueOf(System.nanoTime());
        producerChannel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        long producerLatency = System.nanoTime() - producerStartTime;
        totalProducerLatency.addAndGet(producerLatency);
        messagesProduced.incrementAndGet();

        Channel consumerChannel = consumerChannels.getFirst();
        GetResponse response = consumerChannel.basicGet(QUEUE_NAME, true);
        if (response != null) {
            long deliveryTime = System.nanoTime() - Long.parseLong(new String(response.getBody(), StandardCharsets.UTF_8));
            totalDeliveryLatency.addAndGet(deliveryTime);

            long processingStartTime = System.nanoTime();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            long processingTime = System.nanoTime() - processingStartTime;
            totalConsumerProcessingTime.addAndGet(processingTime);

            messagesConsumed.incrementAndGet();
        }
    }

    @TearDown(Level.Iteration)
    public void tearDown() throws Exception {
        for (Channel channel : producerChannels) {
            channel.close();
        }
        for (Channel channel : consumerChannels) {
            channel.close();
        }
        connection.close();

        System.out.println("Configuration: " + config);
        System.out.println("Messages produced: " + messagesProduced.get());
        System.out.println("Messages consumed: " + messagesConsumed.get());
        System.out.println("Average producer latency (ms): " +
                (messagesProduced.get() > 0 ? totalProducerLatency.get() / messagesProduced.get() / 1_000_000 : 0));
        System.out.println("Average delivery latency (ms): " +
                (messagesConsumed.get() > 0 ? totalDeliveryLatency.get() / messagesConsumed.get() / 1_000_000 : 0));
        System.out.println("Average consumer processing time (ms): " +
                (messagesConsumed.get() > 0 ? totalConsumerProcessingTime.get() / messagesConsumed.get() / 1_000_000 : 0));
        System.out.println("Throughput (messages per second): " +
                (messagesConsumed.get() > 0 ? messagesConsumed.get() / (5 * 10) : 0));
    }
}








