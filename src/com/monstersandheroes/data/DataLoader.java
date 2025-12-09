package com.monstersandheroes.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for reading whitespace delimited tables located in the datas/ directory.
 */
public final class DataLoader {
    private final Path dataDirectory;

    public DataLoader() {
        this(Paths.get("datas"));
    }

    public DataLoader(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public List<String[]> loadTable(String fileName, int columnCount) {
        Path file = dataDirectory.resolve(fileName);
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<String[]> rows = new ArrayList<>();
            boolean headerSkipped = false;
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] tokens = line.split("\\s+");
                if (tokens.length < columnCount) {
                    continue;
                }
                if (tokens.length > columnCount) {
                    tokens = normalize(tokens, columnCount);
                }
                rows.add(tokens);
            }
            return rows;
        } catch (IOException e) {
            throw new DataLoadingException("Failed to read data file: " + fileName, e);
        }
    }

    private String[] normalize(String[] tokens, int columns) {
        String[] normalized = new String[columns];
        int lastIndex = columns - 1;
        for (int i = 0; i < lastIndex; i++) {
            normalized[i] = tokens[i];
        }
        StringBuilder tail = new StringBuilder();
        for (int i = lastIndex; i < tokens.length; i++) {
            if (tail.length() > 0) {
                tail.append(' ');
            }
            tail.append(tokens[i]);
        }
        normalized[lastIndex] = tail.toString();
        return normalized;
    }
}
