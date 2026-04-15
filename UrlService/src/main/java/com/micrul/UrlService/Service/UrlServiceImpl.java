package com.micrul.UrlService.Service;


import com.microp.snowflake.Snowflake;
import com.micrul.UrlService.Outbox.Outbox;
import com.micrul.UrlService.Repository.OutboxRepository;
import com.micrul.UrlService.protobuf.EventProtos;
import com.micrul.UrlService.Config.KafkaConfig.KafkaEventPublisher;
import com.micrul.UrlService.Encoder.Base62EncoderHelper;
import com.micrul.UrlService.Encoder.EncoderBase;
import com.micrul.UrlService.Entity.Url;
import com.micrul.UrlService.Repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Snowflake snowflake;
    private final Base62EncoderHelper base62Encoder;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    /*
     * First, check if long url is available
     * if it is return short url
     * if it is not, save long url and return short url
     * Generate snowflake hash and save hashed value as short url
     * */

    public String shortenUrl(String longUrl) {
        Serializer<Object> serializer = kafkaTemplate.getProducerFactory().getValueSerializer();
        var shortUrl = urlRepository.findUrlByLongUrl(longUrl);

        if (shortUrl == null) {
            System.out.println("Url not found");
            long urlId = snowflake.nextId();
            System.out.println(urlId);

            Url URL = Url.builder()
                    .longUrl(longUrl)
                    .shortUrl(base62Encoder.getEncodedValue(EncoderBase.BASE_0aZ, urlId))
                    .createdAt(Instant.now())
                    .id(urlId)
                    .build();
            EventProtos.UrlCreatedEvent urlCreatedEvent = EventProtos.UrlCreatedEvent.newBuilder()
                    .setLongUrl(longUrl)
                    .setShortUrl(URL.getShortUrl())
                    .build();

            System.out.println(URL.getId());

            urlRepository.save(URL);

            //Serializing event as the same as what kafka protobuf serializer config does.
            //This is the same serializer that is used in kafka protobuf serializer config.
            if(serializer != null){
                byte[] serializedValue = serializer.serialize("url-created-event", urlCreatedEvent);
                Outbox outbox = Outbox.builder()
                        .id(snowflake.nextId())
                        .aggregate_id(urlId)
                        .topic("url-created-event")
                        .type("url-created-event")
                        .aggregate_type("url-created-event")
                        .payload(serializedValue)
                        .timestamp(Instant.now())
                        .build();
                outboxRepository.save(outbox);
            }else {
                throw new RuntimeException("Serializer is null");
            }
            return URL.getShortUrl();
        }
        return shortUrl.getShortUrl();
    }

}
