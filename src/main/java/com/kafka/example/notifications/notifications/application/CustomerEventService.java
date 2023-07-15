package com.kafka.example.notifications.notifications.application;

import com.kafka.example.events.domain.Event;
import com.kafka.example.events.domain.EventType;
import com.kafka.example.events.domain.OrderEvent;
import com.kafka.example.notifications.notifications.infra.port.in.MessagingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventService implements MessagingEvent<Event<?>> {

    @KafkaListener(
            topics = {"orders_created","orders_confirmed"},
            containerFactory = "listenerContainerFactory")
    @Override
    public void consumerOrder(Event<?> event) {
        log.info("Executing listener...");
        if (event.getClass().isAssignableFrom(OrderEvent.class)) {
            OrderEvent orderEventCreated = (OrderEvent) event;
            log.info("Processing message.... id: {}",event.getId());
            if(orderEventCreated.getType().equals(EventType.CREATED)) {
                log.info("The order has been created, we will proceed with the payment .... with Id={}, data={}",
                        orderEventCreated.getId(),
                        orderEventCreated.getData().toString());
            } else if(orderEventCreated.getType().equals(EventType.CONFIRMED)) {
                log.info("The order has been confirmed, with Id={}, data={}",
                        orderEventCreated.getId(),
                        orderEventCreated.getData().toString());
            } else if(orderEventCreated.getType().equals(EventType.REJECTED)) {
                log.info("The order has been rejected. there are not enough funds to carry out the operation, with Id={}, data={}",
                        orderEventCreated.getId(),
                        orderEventCreated.getData().toString());
            }
        }
    }
}
