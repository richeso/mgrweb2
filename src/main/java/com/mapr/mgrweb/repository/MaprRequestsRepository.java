package com.mapr.mgrweb.repository;

import com.mapr.mgrweb.domain.MaprRequests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the MaprRequests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaprRequestsRepository extends MongoRepository<MaprRequests, String> {}
