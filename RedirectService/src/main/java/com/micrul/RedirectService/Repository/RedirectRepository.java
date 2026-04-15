package com.micrul.RedirectService.Repository;

import com.micrul.RedirectService.Entity.RedirectUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedirectRepository extends MongoRepository<RedirectUrl, String> {
    RedirectUrl findByShortUrl(String shortUrl);
}
