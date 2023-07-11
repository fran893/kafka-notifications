package com.kafka.example.notifications.notifications.infra.port.in;

public interface BrokerMessagingConsumer<P, T> {

    P consumerFactory();

    T listenerContainerFactory();

}
