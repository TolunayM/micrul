package com.micrul.UrlService.Controller;

import com.micrul.UrlService.Service.UrlService;
import com.micrul.UrlService.Snowflake.IdGeneratorHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;


    //Todo validate if it's url
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
        return ResponseEntity.ok("https://micrul.com/" + urlService.shortenUrl(longUrl));
    }
}
