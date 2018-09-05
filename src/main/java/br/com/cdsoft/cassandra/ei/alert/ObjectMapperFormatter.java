package br.com.cdsoft.cassandra.ei.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperFormatter implements Formmater {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String format(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
