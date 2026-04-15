package com.micrul.RedirectService.Service;

import com.micrul.RedirectService.Entity.ConsumerEventStore;
import com.micrul.RedirectService.Entity.EventStatus;
import com.micrul.RedirectService.Entity.RedirectUrl;
import com.micrul.RedirectService.Repository.ConsumerEventStoreRepository;
import com.micrul.RedirectService.Repository.RedirectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventPersistService {

    private final RedirectRepository redirectRepository;
    private final ConsumerEventStoreRepository consumerEventStoreRepository;

    @Transactional
    public void processEventInTransaction(long eventId, Runnable handlerLogic) {
        handlerLogic.run();
        consumerEventStoreRepository.save(
                ConsumerEventStore.builder()
                        .eventId(eventId)
                        .eventStatus(EventStatus.SUCCEED)
                        .build()
        );
    }


    /*  I moved this to the DeadLetterPublishingRecoverer
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedEventInNewTransaction(long eventId,String payload, String error) {
        consumerEventStoreRepository.save(
                ConsumerEventStore.builder()
                        .eventId(eventId)
                        .payload(payload)
                        .eventStatus(EventStatus.FAILED)
                        .eventStatusMessage(error)
                        .build()
        );
    }

     */
}
