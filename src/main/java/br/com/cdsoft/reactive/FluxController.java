package br.com.cdsoft.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FluxController {


    private static final int SECONDS = 3;
    private static final double ONE = 1d;
    private static final double ZERO = 0d;
    private List<Double> resultsOfSum = new ArrayList<> ();
    private static final Logger LOGGER = LoggerFactory.getLogger (FluxController.class);


    @GetMapping("/fibonacci")
    @ResponseBody
    public Mono<Double> handler() {

        var lastNumberOnStack =
                resultsOfSum.stream ().reduce ((first, second) -> second).orElse (ZERO);

        resultsOfSum.add (lastNumberOnStack + ONE);

        final double sumOfResult = resultsOfSum.stream ().mapToDouble (Double::doubleValue).sum ();
        return Mono.just (
                sumOfResult

        ).timeout (Duration.ofSeconds (SECONDS))
                .doOnSuccess (result -> {

                    LOGGER.info ("Adding.:  " + (lastNumberOnStack + ONE) + " to " + (sumOfResult - (lastNumberOnStack + ONE)));

                })
         .doFinally (signalType -> LOGGER.info ("Finally with.: " + ((sumOfResult))))
         .doOnError (throwable -> LOGGER.error (throwable.getLocalizedMessage ()));
    }
    @GetMapping(value = "/fibonacci/clear", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Mono clear() {

        return Mono.fromRunnable (() -> {

            resultsOfSum.clear ();

        }).doOnSuccess (consumer -> LOGGER.info ("Clear with sucessful"));


    }
}