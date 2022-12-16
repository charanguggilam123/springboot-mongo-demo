package com.gsc.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gsc.demo.domain.User;
import com.gsc.demo.repo.DocumentRepo;

@RestController
@RequestMapping("/v1/api")
public class DemoController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private DocumentRepo repo;
	
	@GetMapping("/fetchAll")
	public List<User> getData() {
		
		return repo.findAll();
		
	}
	
	@GetMapping("/fetch")
	public List<User> getDataRange(@RequestParam(required = false,defaultValue = "0") int min,@RequestParam(required = true) int max) {
		
		return repo.findUserByIdBetween(min, max);
		
	}
	
	@PostMapping("/add")
	public User addData(@RequestBody final User user) {
		
		return repo.save(user);
		
	}
	
	@GetMapping("/custom")
	public List<Document> getDataCustom() {
		SortOperation sort = Aggregation.sort(Sort.Direction.DESC,"id");
		ProjectionOperation proj = Aggregation.project().andExclude("_id").andExpression("name").as("fullName");
		Aggregation agg = Aggregation.newAggregation(sort,proj);
		
		AggregationResults<Document> response = mongoTemplate.aggregate(agg, User.class, Document.class);
		
		return response.getMappedResults();
		
	}
	
	@GetMapping("/hobbies-data")
	public List<Document> getHobbiesAndCorrespondingCount() {
		UnwindOperation op = Aggregation.unwind("hobbies");
		GroupOperation grp = Aggregation.group("hobbies").count().as("hobbieCount");
		SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "hobbieCount");
//		 Below line to do having like check done in sql
//		MatchOperation match = Aggregation.match(Criteria.where("hobbieCount").gte(3));
		ProjectionOperation proj=Aggregation.project("hobbies").andExpression("hobbieCount").as("count");
		Aggregation agg = Aggregation.newAggregation(op,grp,sortOperation,proj);
		
		AggregationResults<Document> response = mongoTemplate.aggregate(agg, User.class, Document.class);
		
		return response.getMappedResults();
		
	}
	
	@GetMapping("/oldest-in-area")
	public List<Document> getOldestInArea() {
		SortOperation sortOperation= Aggregation.sort(Sort.Direction.DESC, "_id");
		GroupOperation grp = Aggregation.group("address.postalCode").first(Aggregation.ROOT).as("oldest");
		
		ProjectionOperation proj=Aggregation.project().and("oldest.name").as("person").
				and("_id").as("pincode").andInclude("oldest.address").andExclude("_id");
		Aggregation agg = Aggregation.newAggregation(sortOperation,grp,proj);
		
		AggregationResults<Document> response = mongoTemplate.aggregate(agg, User.class, Document.class);
		
		return response.getMappedResults();
		
	}
	
	@GetMapping("/criteria")
	public List<User> getBasedOnCriteria() {
		
		
		List<Criteria> criterias = new ArrayList<>();
		criterias.add(Criteria.where("name").regex("^Sai[a-z A-z]+$"));
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
		return mongoTemplate.find(query, User.class);

		
	}


}
