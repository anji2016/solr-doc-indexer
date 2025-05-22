package com.solr.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solr.model.Document;
import com.solr.model.JsonFeedEntry;

@Service
public class IndexerService {

    @Value("${feed.json-path}")
    private String jsonFilePath; // e.g., "test/feed.json"

    @Value("${feed.delete-all.enabled:false}")
    private boolean deleteAllEnabled;

    @Autowired
    private SolrClient solrClient;

    public void feedContentToSolr() throws IOException, SolrServerException {
        if (deleteAllEnabled) {
            deleteAllDocuments();
        }

        // Load JSON from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new FileNotFoundException("JSON file not found in classpath: " + jsonFilePath);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<JsonFeedEntry> entries = mapper.readValue(inputStream, new TypeReference<>() {});
        for (JsonFeedEntry entry : entries) {
            indexFromJson(entry);
        }
    }

    private void indexFromJson(JsonFeedEntry entry) throws IOException, SolrServerException {
        String content = extractTextFromUrl(entry.getUrl());

        Document doc = new Document();
        doc.setId(entry.getId());
        doc.setTitle(entry.getT());
        doc.setContent(content);
        doc.setCategory(entry.getDocType());
        doc.setSearchQuery(String.join(" ", Arrays.asList(doc.getTitle(), doc.getContent(), doc.getCategory())));
        doc.setTimestamp(Date.from(Instant.now()));

        solrClient.addBean(doc);
        solrClient.commit();
    }

    private String extractTextFromUrl(String url) {
        try {
            org.jsoup.nodes.Document jsoupDoc = Jsoup.connect(url).get();
            return jsoupDoc.text();
        } catch (IOException e) {
            e.printStackTrace();
            return url; // fallback to URL if fetching fails
        }
    }

    public void deleteAllDocuments() throws SolrServerException, IOException {
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }
}

