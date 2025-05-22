package com.solr.controller;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solr.model.Document;
import com.solr.service.FeedGeneratorService;
import com.solr.service.IndexerService;

@RestController
@RequestMapping("/api")
public class SolrController {
	@Autowired
    private SolrClient solrClient;
	
	@Autowired
    private FeedGeneratorService feedGeneratorService;
	
	@Autowired
    private IndexerService indexerService;
    
    @GetMapping("/generate")
    public ResponseEntity<String> generateFeed() throws Exception {
        //feedGeneratorService.feedContentToSolr();
    	indexerService.feedContentToSolr();
        return ResponseEntity.ok("Feed generated");
    }
    
    @GetMapping("/search")
    public List<Document> search(@RequestParam("q") String query)
            throws SolrServerException, IOException {

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("search_query:" + query);
        solrQuery.setStart(0);
        solrQuery.setRows(20);

        QueryResponse response = solrClient.query(solrQuery);
        return response.getBeans(Document.class);
    }

}
