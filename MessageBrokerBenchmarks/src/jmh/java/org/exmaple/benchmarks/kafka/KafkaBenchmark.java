package org.exmaple.benchmarks.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;

import java.time.Duration;
import java.util.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class KafkaBenchmark {

    private static final String TOPIC_NAME = "test-topic";

    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;

    @Param({"1:1", "3:1", "1:3", "3:3", "10:10"})
    private String config;

    private AtomicLong messagesProduced = new AtomicLong(0);
    private AtomicLong messagesConsumed = new AtomicLong(0);
    private AtomicLong totalProducerLatency = new AtomicLong(0);
    private AtomicLong totalDeliveryLatency = new AtomicLong(0);
    private AtomicLong totalConsumerProcessingTime = new AtomicLong(0);

    @Setup(Level.Iteration)
    public void setup() {
        String[] parts = config.split(":");
        int producerCount = Integer.parseInt(parts[0]);
        int consumerCount = Integer.parseInt(parts[1]);

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("acks", "all");
        producerProps.put("linger.ms", "10");
        producerProps.put("batch.size", "65536");
        producer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "benchmark-group");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
    }

    @Benchmark
    @Threads(10)
    public void Messages() {
        long producerStartTime = System.nanoTime();
        String message = String.valueOf(System.nanoTime());
        producer.send(new ProducerRecord<>(TOPIC_NAME, "key", message), (metadata, exception) -> {
            if (exception == null) {
                long producerLatency = System.nanoTime() - producerStartTime;
                totalProducerLatency.addAndGet(producerLatency);
                messagesProduced.incrementAndGet();
            }
        });

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5));
        if (!records.isEmpty()) {
            long processingStartTime = System.nanoTime();
            for (ConsumerRecord<String, String> record : records) {
                long sendTimestamp = Long.parseLong(record.value());
                long deliveryLatency = System.nanoTime() - sendTimestamp;
                totalDeliveryLatency.addAndGet(deliveryLatency);

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
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        producer.close();
        consumer.close();

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




