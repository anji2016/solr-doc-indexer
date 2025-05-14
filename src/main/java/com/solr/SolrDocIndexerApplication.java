package com.solr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.solr"})
public class SolrDocIndexerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolrDocIndexerApplication.class, args);
	}

}
