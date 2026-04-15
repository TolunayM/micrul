package com.micrul.RedirectService.Service;

import com.micrul.RedirectService.Repository.RedirectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectServiceImpl implements RedirectService{

    private final RedirectRepository redirectRepository;

    @Cacheable(value = "redirect",key = "#shortUrl")
    public String checkRedirectUrl(String shortUrl) {
        var redirectUrl = redirectRepository.findByShortUrl(shortUrl);
        System.out.println(redirectUrl.getId());
        return redirectUrl.getLongUrl();
    }

}
