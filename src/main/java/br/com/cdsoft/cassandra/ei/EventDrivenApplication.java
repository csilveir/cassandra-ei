package br.com.cdsoft.cassandra.ei;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;


@SpringBootApplication
@EnableWebFlux
public class EventDrivenApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EventDrivenApplication.class);

    }
}
