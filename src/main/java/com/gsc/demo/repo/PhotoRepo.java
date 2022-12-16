package com.gsc.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gsc.demo.domain.Photo;

@Repository
public interface PhotoRepo extends MongoRepository<Photo, String> {

}
