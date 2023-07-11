package com.kafka.example.notifications.notifications.infra.port.in;

public interface MessagingEvent<T> {

    void consumer(T t);

}
