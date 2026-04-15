package com.micrul.UrlService.Service;


import com.micrul.UrlService.Config.KafkaConfig.KafkaEventPublisher;
import com.micrul.UrlService.Outbox.Outbox;
import com.micrul.UrlService.Repository.OutboxRepository;
import com.micrul.UrlService.protobuf.EventProtos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@RequiredArgsConstructor
@Slf4j
@Component
@Deprecated
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaEventPublisher kafkaEventPublisher;



    void publish(Object T,ArrayList<Outbox> outboxList) {
        var event = T;
//        Parser<T> urlCreatedEventParser = EventProtos.UrlCreatedEvent.parser();
        if(!outboxList.isEmpty()) {
            for(Outbox outbox : outboxList) {
                if(outbox.getTopic().equals("url-created-event")) {
                    try {
                        //This could be changed to sending just the payload as a byte[]
                        //Kafka publisher sends to byte[], and the consumer uses a dynamic message
//                        event = EventProtos.UrlCreatedEvent.parseFrom(outbox.getPayload());
                    }catch (Exception e) {
                        log.error("Error parsing event payload",e);
//                        outbox.setStatus("FAILED");
                        outboxRepository.save(outbox);
                        continue;
                    }
                }

                try{
//                    outbox.setStatus("SUCCESS");
                    kafkaEventPublisher.sendUrlEvent(event, outbox.getTopic(), String.valueOf(outbox.getId()));
                    outboxRepository.save(outbox);
                }catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error sending event {}",event);
//                    outbox.setStatus("FAILED");
                    outboxRepository.save(outbox);
                }


            }
        }else{
            log.info("Outbox table is empty");
        }

    }


//    @Scheduled(fixedDelay = 1000)
//    void scheduledPublisher(){
//        ArrayList<Outbox> outboxList = outboxRepository.findAllByStatus("PENDING");
//
//        if(!outboxList.isEmpty()) {
//            for(Outbox outbox : outboxList) {
//                if(outbox.getTopic().equals("url-created-event")) {
//                    publish(EventProtos.UrlCreatedEvent.getDefaultInstance(),outboxList);
//                }
//            }
//        }
//    }

    //TODO handle failed events
}
