package com.micrul.RedirectService.Config.KafkaConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUrlEvent(Object urlEvent, String topic,String key) {
        log.info("Sending url event to topic {}", topic);
        kafkaTemplate.send(topic, key,urlEvent);
    }
}
