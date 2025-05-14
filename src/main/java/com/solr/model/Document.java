package com.solr.model;

import org.apache.solr.client.solrj.beans.Field;
import lombok.Data;

@Data
public class Document {
	@Field
	private String id;
	@Field
	private String title;
	@Field
	private String content;
	@Field
	private String category;
	@Field
    private String timestamp;
	@Field("search_query")
	private String searchQuery; // Store the search query string
	
	// Optional method to set the timestamp to the current time
    public void setTimestampToNow() {
        // Get the current timestamp in ISO 8601 format
        this.timestamp = java.time.Instant.now().atOffset(java.time.ZoneOffset.UTC)
                .format(java.time.format.DateTimeFormatter.ISO_INSTANT);
    }
}
