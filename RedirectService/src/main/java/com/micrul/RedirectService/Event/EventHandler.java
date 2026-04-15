package com.micrul.RedirectService.Event;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventHandler<K> {
    String supportedTopic();
    void handle(K payload);
}
