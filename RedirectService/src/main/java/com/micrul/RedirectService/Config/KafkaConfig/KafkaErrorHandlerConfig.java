package com.micrul.RedirectService.Config.KafkaConfig;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaTemplate<String, Object> template) {

        return new DeadLetterPublishingRecoverer(template,
                (ConsumerRecord<?, ?> record, Exception exception) -> {
                    log.error("Failed event := Topic: {}, Partition: {}, Offset: {}, Hata: {}",
                            record.topic(), record.partition(), record.offset(), exception.getMessage());

                    return new TopicPartition(record.topic() + ".DLT", record.partition());
                });
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {

        FixedBackOff backOff = new FixedBackOff(1000L, 3L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        return errorHandler;
    }
}
