package com.micrul.RedirectService.Event;


import com.google.protobuf.Message;
import com.micrul.RedirectService.Entity.RedirectUrl;
import com.micrul.RedirectService.Mapper.UrlMapper;
import com.micrul.RedirectService.Repository.RedirectRepository;
import com.micrul.RedirectService.Service.EventPersistService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlCreatedEventRetryHandler implements EventHandler<ConsumerRecord<String, Message>> {

    private final EventPersistService eventPersistService;
    private final UrlMapper urlMapper;
    private final RedirectRepository redirectRepository;

    @Override
    public String supportedTopic() {
        return "outbox.event.url-created-event.DLT";
    }

    @Override
    public void handle(ConsumerRecord<String, Message> payload) {

        RedirectUrl redirectUrl = urlMapper.mapToRedirectUrl(payload);

        eventPersistService.processEventInTransaction(
                redirectUrl.getId(),
                () -> redirectRepository.save(redirectUrl));
    }
}
