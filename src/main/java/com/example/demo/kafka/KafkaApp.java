package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class KafkaApp implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private Consumer<String, String> kafkaConsumer;

    @Value("${kafka.topic}")
    private String topic;

    @Override
    public void run(String... args) {
        startKafkaProducer();
        startKafkaConsumer();
    }

    private void startKafkaProducer() {
        for (int i = 0; i < 10; i++) {
            String message = "Hello from Java using SSL " + (i + 1) + "!";
            kafkaTemplate.send(topic, message);
            System.out.println("Message sent: " + message);
            sleep();
        }
    }

    private void startKafkaConsumer() {
        kafkaConsumer.subscribe(List.of(topic));

        while (true) {
            ConsumerRecords<String, String> messages = kafkaConsumer.poll(Duration.ofMillis(100));
            messages.forEach(record -> {
                System.out.println("Received message from " + topic + ": " + record.value());
            });
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
