package com.solr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solr.service.FeedGeneratorService;

@RestController
public class SolrController {
	@Autowired
    private FeedGeneratorService feedGeneratorService;
    
    @GetMapping("/generate")
    public ResponseEntity<String> generateFeed() throws Exception {
        feedGeneratorService.generateFeed();
        return ResponseEntity.ok("Feed generated");
    }

}
