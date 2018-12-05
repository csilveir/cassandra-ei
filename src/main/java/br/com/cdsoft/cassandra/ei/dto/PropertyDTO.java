package br.com.cdsoft.cassandra.ei.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class PropertyDTO implements Serializable {

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private UUID uuid;

    @Override
    public String toString() {
        return "PropertyDTO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", dtProperty=" + dtProperty +
                '}';
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDtProperty(Date dtProperty) {
        this.dtProperty = dtProperty;
    }

    private String key;
    private String value;

    public TypeProperty getType() {
        return type;
    }

    public void setType(TypeProperty type) {
        this.type = type;
    }

    private TypeProperty type;
    public PropertyDTO() {

    }

    public PropertyDTO(String key, String value, Date dtProperty) {
        this.key = key;
        this.value = value;
        this.dtProperty = dtProperty;
    }
    public PropertyDTO(UUID uuid, String key, String value, Date dtProperty) {
        this(key, value, dtProperty);
        this.uuid = uuid;
    }
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyDTO that = (PropertyDTO) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value) &&
                Objects.equals(dtProperty, that.dtProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, dtProperty);
    }

    public Date getDtProperty() {
        return dtProperty;
    }
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")

    private Date dtProperty;
}
