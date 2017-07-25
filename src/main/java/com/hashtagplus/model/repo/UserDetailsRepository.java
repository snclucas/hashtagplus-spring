package com.hashtagplus.model.repo;

import com.hashtagplus.model.HtplUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserDetailsRepository extends MongoRepository<HtplUserDetails, String> {

    HtplUserDetails findByUsername(String userName);

    HtplUserDetails findByToken(String token);

}