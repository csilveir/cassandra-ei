package br.com.cdsoft.cassandra.ei.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class KafkaErrorHandler implements ContainerAwareErrorHandler {

    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records,
                       Consumer<?, ?> consumer, MessageListenerContainer container) {


        var exception = thrownException;


        System.out.println(exception);

    }


}
