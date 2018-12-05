package br.com.cdsoft.cassandra.ei.api;

import br.com.cdsoft.cassandra.ei.business.PropertyService;
import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class PropertyController {


    private static final int SECONDS = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);


    @Autowired
    PropertyService propertyService;

    @GetMapping("/property/{property}")
    @ResponseBody
    public Mono<List<PropertyDTO>> getProperty(@PathVariable("property") final String property) {

        return Mono.just(
                propertyService.getProperty(property)

        )
                .timeout(Duration.ofSeconds(SECONDS))
                .doFinally(signalType -> LOGGER.info("Finally returning properties"))
                .doOnError(throwable -> LOGGER.error(throwable.getLocalizedMessage()));
    }

    @GetMapping("/property")
    @ResponseBody
    public Flux<List<PropertyDTO>> properties() {


        return Flux.just(propertyService.getProperties()).timeout(Duration.ofSeconds(SECONDS))
                .sort(Comparator.comparing(property -> ((PropertyDTO) property).getDtProperty()))
                .onErrorReturn(Collections.emptyList())
                .doFinally(signalType -> LOGGER.info("Finally returning properties"))
                .doOnError(throwable -> LOGGER.error(throwable.getLocalizedMessage()));


    }


}