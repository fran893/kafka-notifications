package com.kafka.example.notifications.notifications.infra.adapter.in;

import com.kafka.example.events.domain.Event;
import com.kafka.example.notifications.notifications.infra.port.in.BrokerMessagingConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig implements BrokerMessagingConsumer<ConsumerFactory<String, Event<?>>, ConcurrentKafkaListenerContainerFactory<String, Event<?>>> {

    private final String bootstrapAddress = "localhost:29092";
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offset;


    @Bean
    @Override
    public ConsumerFactory<String, Event<?>> consumerFactory() {
        Map<String, String> props = new HashMap<>();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);

        props.put(JsonSerializer.TYPE_MAPPINGS,
                "com.kafka.example:com.kafka.example.events.domain.CustomerCreatedEvent");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);


        final JsonDeserializer<Event<?>> jsonDeserializer = new JsonDeserializer<>();

        return new DefaultKafkaConsumerFactory(
                props,
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    @Override
    public ConcurrentKafkaListenerContainerFactory<String, Event<?>> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Event<?>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
