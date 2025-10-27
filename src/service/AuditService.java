package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static final String FILE_NAME = "audit.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logAction(String action) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.append(action).append(",").append(timestamp).append("\n");
        } catch (IOException e) {
            System.err.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}
