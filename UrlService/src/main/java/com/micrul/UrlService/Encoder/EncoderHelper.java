package com.micrul.UrlService.Encoder;

public interface EncoderHelper<E,T> {
    T getEncodedValue(EncoderBase base,E value);
}
