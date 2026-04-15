package com.micrul.RedirectService.Repository;

import com.micrul.RedirectService.Entity.ConsumerEventStore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerEventStoreRepository extends MongoRepository<ConsumerEventStore, Long> {
}
