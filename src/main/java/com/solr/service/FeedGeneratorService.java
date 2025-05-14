package com.solr.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solr.model.Document;

@Service
public class FeedGeneratorService {
	@Value("${feed.file-path}")
	private String filePath;
	@Autowired
	private SolrClient solrClient;
	
	public void generateFeed() throws Exception {
        List<String> paths = Files.readAllLines(Paths.get(filePath));
        List<Document> docs = new ArrayList<>();

        for (String pathStr : paths) {
            Path path = Paths.get(pathStr.trim());
            String category = getCategory(pathStr);

            Document doc = new Document();
            doc.setId(UUID.randomUUID().toString());
            doc.setCategory(category);
            doc.setTitle(path.getFileName().toString());

            if (pathStr.endsWith(".pdf")) {
                doc.setContent(extractPdfText(path));
            } else if (pathStr.endsWith(".js") || pathStr.endsWith(".jsx") || pathStr.endsWith(".html")) {
                doc.setContent(Files.readString(path));
            } else {
                continue;
            }

            doc.setSearchQuery(doc.getTitle() + " " + doc.getContent() + " " + doc.getCategory());
            docs.add(doc);
        }

        solrClient.addBeans(docs);
        solrClient.commit();
    }

	private String extractPdfText(Path path) throws IOException {
	    try (PDDocument document = Loader.loadPDF(path.toFile())) {
	        PDFTextStripper stripper = new PDFTextStripper();
	        return stripper.getText(document);
	    }
	}

    private String getCategory(String path) {
        if (path.contains("react")) return "react";
        if (path.contains("pdf")) return "pdf";
        return "unknown";
    }
}
