package Grupotextil.SDI.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoOrdenConverter implements AttributeConverter<OrdenProduccion.EstadoOrden, String> {

    @Override
    public String convertToDatabaseColumn(OrdenProduccion.EstadoOrden estado) {
        if (estado == null) {
            return null;
        }
        return estado.getValor();
    }

    @Override
    public OrdenProduccion.EstadoOrden convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (OrdenProduccion.EstadoOrden estado : OrdenProduccion.EstadoOrden.values()) {
            if (estado.getValor().equals(dbData)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + dbData);
    }
} 