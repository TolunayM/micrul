package com.micrul.UrlService.Encoder;


import org.springframework.stereotype.Component;

@Component
public class Base62EncoderHelper implements EncoderHelper<Long,String> {

    @Override
    public String getEncodedValue(EncoderBase base, Long id) {
        return new Base62Encoder(base).encode(id);
    }
}
