package com.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {
	@Value("${solr.url}")
	private String solrUrl;
	
	@Bean
    public SolrClient solrClient() {
        return new Http2SolrClient.Builder(solrUrl).build();
    }

}
