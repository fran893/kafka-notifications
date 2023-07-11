package com.kafka.example.notifications.notifications.application;

import com.kafka.example.events.domain.CustomerCreatedEvent;
import com.kafka.example.events.domain.Event;
import com.kafka.example.notifications.notifications.infra.port.in.MessagingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventService implements MessagingEvent<Event<?>> {

    @KafkaListener(
            topics = "${topic.customer.name:customers}",
            containerFactory = "listenerContainerFactory",
            groupId = "grupo1")
    @Override
    public void consumer(Event<?> event) {
        if (event.getClass().isAssignableFrom(CustomerCreatedEvent.class)) {
            CustomerCreatedEvent customerCreatedEvent = (CustomerCreatedEvent) event;
            log.info("Received Customer created event .... with Id={}, data={}",
                    customerCreatedEvent.getId(),
                    customerCreatedEvent.getData().toString());
        }
    }
}
