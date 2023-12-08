package com.example.demo.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.properties.ssl.truststore.password}")
    private String trustStorePassword;

    @Value("${kafka.properties.ssl.keystore.type}")
    private String keyStoreType;

    @Value("${kafka.properties.ssl.keystore.password}")
    private String keyStorePassword;

    @Value("${kafka.properties.ssl.key.password}")
    private String keyPassword;

    @Bean
    public ProducerFactory<String, String> producerFactory() throws IOException, URISyntaxException {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put("security.protocol", "SSL");

        ClassPathResource trustStoreResource = new ClassPathResource("kafka1/client.truststore.jks");
        URL trustStoreUrl = trustStoreResource.getURL();
        Path trustStorePath = Paths.get(trustStoreUrl.toURI());
        configProps.put("ssl.truststore.location", trustStorePath.toString());
        configProps.put("ssl.truststore.password", trustStorePassword);

        // Load keystore properties
        ClassPathResource keyStoreResource = new ClassPathResource("kafka1/client.keystore.p12");
        URL keyStoreUrl = keyStoreResource.getURL();
        Path keyStorePath = Paths.get(keyStoreUrl.toURI());
        configProps.put("ssl.keystore.type", keyStoreType);
        configProps.put("ssl.keystore.location", keyStorePath.toString());
        configProps.put("ssl.keystore.password", keyStorePassword);
        configProps.put("ssl.key.password", keyPassword);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() throws IOException, URISyntaxException {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() throws IOException, URISyntaxException {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put("security.protocol", "SSL");
        ClassPathResource trustStoreResource = new ClassPathResource("kafka1/client.truststore.jks");
        URL trustStoreUrl = trustStoreResource.getURL();
        Path trustStorePath = Paths.get(trustStoreUrl.toURI());
        configProps.put("ssl.truststore.location", trustStorePath.toString());
        configProps.put("ssl.truststore.password", trustStorePassword);
        ClassPathResource keyStoreResource = new ClassPathResource("kafka1/client.keystore.p12");
        URL keyStoreUrl = keyStoreResource.getURL();
        Path keyStorePath = Paths.get(keyStoreUrl.toURI());
        configProps.put("ssl.truststore.location", trustStorePath.toString());
        configProps.put("ssl.truststore.password", trustStorePassword);
        configProps.put("ssl.keystore.type", keyStoreType);
        configProps.put("ssl.keystore.location", keyStorePath.toString());
        configProps.put("ssl.keystore.password", keyStorePassword);
        configProps.put("ssl.key.password", keyPassword);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public Consumer<String, String> kafkaConsumer() throws IOException, URISyntaxException {
        return new KafkaConsumer<>(consumerFactory().getConfigurationProperties());
    }
}
