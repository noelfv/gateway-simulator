package com.bbva.gui.commons;
/*
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFProcessor {
    private String documentContent;
    private List<String> chunks;

    public PDFProcessor(String pdfPath) throws IOException {
        this.documentContent = extractTextFromPDF(pdfPath);
        this.chunks = createChunks(documentContent, 1000); // Chunks de 1000 caracteres
    }

    private String extractTextFromPDF(String pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private List<String> createChunks(String text, int maxChunkSize) {
        List<String> chunks = new ArrayList<>();
        String[] paragraphs = text.split("\n\n");

        StringBuilder currentChunk = new StringBuilder();

        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() > maxChunkSize) {
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString().trim());
                    currentChunk = new StringBuilder();
                }
            }
            currentChunk.append(paragraph).append("\n\n");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }

    public List<String> findRelevantChunks(String query, int maxChunks) {
        List<String> relevantChunks = new ArrayList<>();
        String[] queryWords = query.toLowerCase().split("\\s+");

        for (String chunk : chunks) {
            String lowerChunk = chunk.toLowerCase();
            int relevanceScore = 0;

            for (String word : queryWords) {
                if (lowerChunk.contains(word)) {
                    relevanceScore++;
                }
            }

            if (relevanceScore > 0) {
                relevantChunks.add(chunk);
            }
        }

        return relevantChunks.subList(0, Math.min(maxChunks, relevantChunks.size()));
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public List<String> getChunks() {
        return chunks;
    }
}
*/