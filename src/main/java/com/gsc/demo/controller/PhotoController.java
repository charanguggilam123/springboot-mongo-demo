package com.gsc.demo.controller;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gsc.demo.domain.Photo;
import com.gsc.demo.repo.PhotoRepo;

@RestController
@RequestMapping("/v1/photo")
public class PhotoController {
	
	@Autowired
	private PhotoRepo repo;
	
	@PostMapping
	public String addPhoto(@RequestParam("image") MultipartFile imageFile) throws IOException {
		Photo photo = new Photo();
		photo.setTitle(imageFile.getOriginalFilename());
		photo.setPhoto(new Binary(BsonBinarySubType.BINARY,imageFile.getBytes()));
		return repo.save(photo).getId();
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource> downloadPhoto(@PathVariable("id") String photoId){
		
		Photo photo=repo.findById(photoId).get();
		Resource res = new ByteArrayResource(photo.getPhoto().getData(), photo.getTitle());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getTitle() + "\"");
		responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString());
		return new ResponseEntity<>(res,responseHeaders, HttpStatus.OK);
		
	}

}
