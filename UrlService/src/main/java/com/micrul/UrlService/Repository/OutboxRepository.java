package com.micrul.UrlService.Repository;

import com.micrul.UrlService.Outbox.Outbox;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OutboxRepository extends MongoRepository<Outbox, Long> {
}
