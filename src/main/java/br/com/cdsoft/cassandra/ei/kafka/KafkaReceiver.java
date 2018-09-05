package br.com.cdsoft.cassandra.ei.kafka;

import br.com.cdsoft.cassandra.ei.business.PropertyService;
import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import br.com.cdsoft.cassandra.ei.http.SlackAlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiver {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    PropertyService propertyService;


    @Autowired
    SlackAlertService slackAlertService;

    private static final Logger LOG = LoggerFactory.getLogger(KafkaReceiver.class);

    @KafkaListener(topics = "${app.topic}")
    public void listen(@Payload String message) {
        LOG.info("received message='{}'", message);
        System.out.println("received message='{}'" +  message);
        try {
            var property = objectMapper.readValue(message   , PropertyDTO.class);
            propertyService.insertProperty(property.getKey(), property.getValue());
            slackAlertService.send(property);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}