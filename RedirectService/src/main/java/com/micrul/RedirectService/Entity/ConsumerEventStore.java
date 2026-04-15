package com.micrul.RedirectService.Entity;


import com.google.protobuf.Message;
import com.microp.snowflake.SnowflakeData;
import com.microp.snowflake.SnowflakeId;
import lombok.*;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SnowflakeData
@Document(collection = "eventStore")
public class ConsumerEventStore {

    @Id
    private long eventId;
    private String record;
    private EventStatus eventStatus;
    private String payload;
    private String eventStatusMessage;
}
