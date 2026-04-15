package com.micrul.UrlService.Entity;


import com.microp.snowflake.SnowflakeData;
import com.microp.snowflake.SnowflakeId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "url")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SnowflakeData
public class Url {

    @Id
    @SnowflakeId
    private long id;
    private String shortUrl;
    private String longUrl;
    private String hashValue;
    private Instant createdAt;
    private long visitorCount;
    private long uniqueVisitorCount;
}
