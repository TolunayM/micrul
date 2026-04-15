package com.micrul.RedirectService.Event;

import com.google.protobuf.Message;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventDispatcher {

    private final Map<String, EventHandler<ConsumerRecord<String,Message>> > eventHandlers;

    public EventDispatcher(List<EventHandler<ConsumerRecord<String,Message>> > handlerList) {
        this.eventHandlers = handlerList.stream()
                .collect(Collectors.toMap(EventHandler::supportedTopic, h -> h));
    }

    public void dispatch(ConsumerRecord<String, Message> record) {
        EventHandler<ConsumerRecord<String,Message>> handler = eventHandlers.get(record.topic());
        if (handler == null) throw new IllegalArgumentException("No handler for topic: " + record.topic());

        System.out.println("Dispatching event: " + record);
        handler.handle(record);
    }
}
