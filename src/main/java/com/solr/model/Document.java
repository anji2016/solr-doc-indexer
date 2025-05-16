package com.solr.model;

import java.util.Date;

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
	private Date timestamp; 
	@Field("search_query")
	private String searchQuery; // Store the search query string
}
