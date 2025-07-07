package Grupotextil.SDI.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }
        // Convertir Duration a formato INTERVAL de PostgreSQL
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        
        return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        try {
            // Parsear formato INTERVAL de PostgreSQL
            String[] parts = dbData.split(":");
            if (parts.length >= 3) {
                long hours = Long.parseLong(parts[0]);
                long minutes = Long.parseLong(parts[1]);
                long seconds = Long.parseLong(parts[2]);
                
                return Duration.ofSeconds(hours * 3600 + minutes * 60 + seconds);
            }
            return Duration.ZERO;
        } catch (Exception e) {
            return Duration.ZERO;
        }
    }
} 