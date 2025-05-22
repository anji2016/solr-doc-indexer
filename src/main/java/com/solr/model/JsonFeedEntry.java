package com.solr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JsonFeedEntry {
	@JsonProperty("DocType")
    private String docType;

    @JsonProperty("AppName")
    private String appName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("p")
    private String p;

    @JsonProperty("a")
    private String a;

    @JsonProperty("id")
    private String id;

    @JsonProperty("t")
    private String t;

    @JsonProperty("d")
    private String d;

    @JsonProperty("url")
    private String url;
}
