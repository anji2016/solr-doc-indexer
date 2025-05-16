package com.solr.service;

import com.solr.model.Document;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FeedGeneratorService {

    @Value("${feed.file-path}")
    private String filePath;

    @Value("${feed.delete-all.enabled:false}")
    private boolean deleteAllEnabled;

    @Autowired
    private SolrClient solrClient;

    public void feedContentToSolr() throws IOException, SolrServerException {
        if (deleteAllEnabled) {
            deleteAllDocuments();
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            Path path = Paths.get(line.trim());
            String fileName = path.getFileName().toString().toLowerCase();

            if (fileName.endsWith(".pdf")) {
                processAndIndex(path, "pdf", extractTextFromPdf(path));
            } else if (fileName.endsWith(".jsx")) {
                processAndIndex(path, "jsx", extractTextFromJsx(path));
            }
        }
    }

    private void processAndIndex(Path path, String category, String content) throws IOException, SolrServerException {
        if (content == null || content.isBlank()) return;

        Document doc = new Document();
        doc.setId(generateIdFromPath(path));
        doc.setTitle(path.getFileName().toString());
        doc.setContent(content);
        doc.setCategory(category);
        doc.setSearchQuery(doc.getTitle() + " " + doc.getContent() + " " + doc.getCategory());
        doc.setTimestamp(Instant.now().toString());

        solrClient.addBean(doc);
        solrClient.commit();
    }

    private String extractTextFromPdf(Path path) throws IOException {
        try (PDDocument pdf = Loader.loadPDF(path.toFile())) {
            return new PDFTextStripper().getText(pdf);
        }
    }

    private String extractTextFromJsx(Path path) throws IOException {
        String rawContent = Files.readString(path);
        StringBuilder extractedHtml = new StringBuilder();

        Matcher matcher = Pattern.compile(">([^<]+)<").matcher(rawContent);
        while (matcher.find()) {
            extractedHtml.append(matcher.group(1).trim()).append(" ");
        }
        return extractedHtml.toString().trim();
    }

    public void deleteAllDocuments() throws SolrServerException, IOException {
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }

    public void deleteDocumentById(String id) throws SolrServerException, IOException {
        solrClient.deleteById(id);
        solrClient.commit();
    }

    private String generateIdFromPath(Path path) {
        return path.toAbsolutePath().toString().replace("\\", "/");
    }
}