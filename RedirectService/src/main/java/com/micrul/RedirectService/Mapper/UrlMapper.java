package com.micrul.RedirectService.Mapper;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import com.micrul.RedirectService.Entity.RedirectUrl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    public RedirectUrl mapToRedirectUrl(ConsumerRecord<String, Message> eventMessage) {
        var valueDescriptor = eventMessage.value().getDescriptorForType();
        var value = eventMessage.value();
        var id = eventMessage.key();
        return RedirectUrl.builder()
                .id(Long.parseLong(id))
                .shortUrl(value.getField(valueDescriptor.findFieldByName("short_url")).toString())
                .longUrl(value.getField(valueDescriptor.findFieldByName("long_url")).toString())
                .build();
    }
}
