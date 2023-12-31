package org.tenten.tentenstomp.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.producer}")
    private String host;
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public Map<String, Object> producerConfigurations() {
        HashMap<String, Object> producerConfigMap = new HashMap<>();
        producerConfigMap.put(BOOTSTRAP_SERVERS_CONFIG, host);
        producerConfigMap.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigMap.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return producerConfigMap;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
