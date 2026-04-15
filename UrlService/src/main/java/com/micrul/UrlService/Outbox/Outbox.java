package com.micrul.UrlService.Outbox;


import com.google.protobuf.Message;
import com.microp.snowflake.SnowflakeData;
import com.microp.snowflake.SnowflakeId;
import com.micrul.UrlService.protobuf.EventProtos;
import lombok.*;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;


@Document(collection = "outbox")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SnowflakeData
public class Outbox {

    @Id
    @SnowflakeId
    private long id;
    private String topic;
    private String aggregate_type;
    private String type;
    private byte[] payload;
    @Field(name = "aggregate_id")
    private long aggregate_id;
    private Instant timestamp;
}
