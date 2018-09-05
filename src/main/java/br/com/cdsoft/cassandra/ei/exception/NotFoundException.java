package br.com.cdsoft.cassandra.ei.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No found entity")  // 404
    public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }
}