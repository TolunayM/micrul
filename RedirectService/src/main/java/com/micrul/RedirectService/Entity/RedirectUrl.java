package com.micrul.RedirectService.Entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "redirectUrl")
public class RedirectUrl {

    private long id;
    private String shortUrl;
    private String longUrl;
}
