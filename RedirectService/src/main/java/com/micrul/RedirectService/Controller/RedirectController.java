package com.micrul.RedirectService.Controller;


import com.micrul.RedirectService.Service.RedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/redirect")
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirect(@PathVariable String shortUrl) {
        String redirectUrl = redirectService.checkRedirectUrl(shortUrl);
        redirectUrl = redirectUrl.startsWith("http") ? redirectUrl : "https://" + redirectUrl;
        System.out.println(shortUrl);
        return ResponseEntity
                .status(302)
                .location(URI.create(redirectUrl))
                .build();
    }
}
