package com.micrul.RedirectService.Service;


import com.google.protobuf.*;
import com.micrul.RedirectService.Config.KafkaConfig.KafkaEventPublisher;
import com.micrul.RedirectService.Entity.RedirectUrl;
import com.micrul.RedirectService.Event.EventDispatcher;
import com.micrul.RedirectService.Mapper.UrlMapper;
import com.micrul.RedirectService.Repository.ConsumerEventStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ListenerServiceImpl implements ListenerService {

    private final ConsumerEventStoreRepository consumerEventStoreRepository;
    private final UrlMapper urlMapper;
    private final EventPersistService eventPersistService;
    private final EventDispatcher eventDispatcher;

    @KafkaListener(topics = "outbox.event.url-created-event",groupId = "redirect-service")
    public void persistUrl(@Payload ConsumerRecord<String, Message> urlInfo){

        RedirectUrl payloadUrl = urlMapper.mapToRedirectUrl(urlInfo);
        long eventId = payloadUrl.getId();

        if(!consumerEventStoreRepository.existsById(eventId)){
                eventDispatcher.dispatch(urlInfo);
        }

        /* I moved this to the DeadLetterPublishingRecoverer

        if(!consumerEventStoreRepository.existsById(eventId)){
            try{
                eventDispatcher.dispatch(urlInfo);
            }catch (Exception e){
                long id = Long.parseLong(urlInfo.key());
                var value = String.valueOf(urlInfo.value());
                eventPersistService.saveFailedEventInNewTransaction(id, value, e.getMessage());
            }
        }

         */


    }


    @KafkaListener(topics = "outbox.event.url-created-event.DLT",groupId = "redirect-service-retry")
    public void retryDeadLetter(@Payload ConsumerRecord<String, Message> urlDeadLetter){
        RedirectUrl payloadUrl = urlMapper.mapToRedirectUrl(urlDeadLetter);
        long eventId = payloadUrl.getId();

        System.out.println("Retrying event: " + eventId);
        System.out.println("DLT: " + urlDeadLetter);

        if(!consumerEventStoreRepository.existsById(eventId)){
            eventDispatcher.dispatch(urlDeadLetter);
        }

    }
}
