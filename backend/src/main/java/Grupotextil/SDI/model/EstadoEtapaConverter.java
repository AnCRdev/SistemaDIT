package Grupotextil.SDI.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoEtapaConverter implements AttributeConverter<EtapaAsignada.EstadoEtapa, String> {

    @Override
    public String convertToDatabaseColumn(EtapaAsignada.EstadoEtapa estado) {
        if (estado == null) {
            return null;
        }
        return estado.getValor();
    }

    @Override
    public EtapaAsignada.EstadoEtapa convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (EtapaAsignada.EstadoEtapa estado : EtapaAsignada.EstadoEtapa.values()) {
            if (estado.getValor().equals(dbData)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + dbData);
    }
} 