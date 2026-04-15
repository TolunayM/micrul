package com.micrul.UrlService.Repository;

import com.micrul.UrlService.Entity.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url, Long> {
    boolean existsUrlByLongUrl(String longUrl);
    Url findUrlByLongUrl(String longUrl);
}
