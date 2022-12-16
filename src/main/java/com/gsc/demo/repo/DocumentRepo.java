package com.gsc.demo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.gsc.demo.domain.User;
@Repository
public interface DocumentRepo extends MongoRepository<User, Integer> {
	
	@Query(value = "{ '_id' : { $gt : ?0, $lt : ?1}}",
	           fields = "{addresses:  0}")
	    List<User> findUserByIdBetween(Integer min, Integer max);

}
