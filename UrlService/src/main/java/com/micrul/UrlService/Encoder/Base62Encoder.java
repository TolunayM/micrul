package com.micrul.UrlService.Encoder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Base62Encoder implements Encoder<Long> {

    private final String encodingBase;
    private final String BASE_0aZ = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String BASE_0Az = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final String BASE_a0Z = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String BASE_aZ0 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final String BASE_A0z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private final String BASE_Az0 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public Base62Encoder(EncoderBase base){
        switch(base){
            case BASE_0aZ:
                encodingBase = this.BASE_0aZ;
                break;
            case BASE_0Az:
                encodingBase = this.BASE_0Az;
                break;
            case BASE_a0Z:
                encodingBase = this.BASE_a0Z;
                break;
            case BASE_aZ0:
                encodingBase = this.BASE_aZ0;
                break;
            case BASE_A0z:
                encodingBase = this.BASE_A0z;
                break;
            case BASE_Az0:
                encodingBase = this.BASE_Az0;
                break;
            default:
                throw new IllegalArgumentException("Invalid base");
        }
    }


    //Todo check this logic later
    @Override
    public String encode(Long urlId){
        if(urlId == null){
            throw new IllegalArgumentException("Url id can't be null");
        }
        StringBuilder encodedId = new StringBuilder();
        while(urlId > 0){
            encodedId.append(encodingBase.charAt((int)(urlId % 62)));
            urlId /= 62;
        }
        return encodedId.reverse().toString();

    }



}
