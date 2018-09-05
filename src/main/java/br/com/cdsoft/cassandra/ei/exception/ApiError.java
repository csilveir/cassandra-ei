package br.com.cdsoft.cassandra.ei.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiError {

    private Integer statusCode;
    private String error;
    private List<String> messages;

    private ApiError(Integer statusCode, String error, String message) {
        this(statusCode, error, Arrays.asList(message));
    }

    private ApiError(Integer statusCode, String error, List<String> messages) {
        this.statusCode = statusCode;
        this.error = error;
        this.messages = messages;
    }

    public static ApiError fromHttpError(HttpStatus httpStatus, Exception exception) {

        return new ApiError(httpStatus.value(), httpStatus.getReasonPhrase(), exception.getMessage());

    }

    public static ApiError fromMessage(HttpStatus httpStatus, String message) {
        return new ApiError(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

    public static ApiError fromBindingResult(BindingResult bindingResult) {
        List<String> erros = bindingResult
                .getAllErrors()
                .stream()
                .map(error -> {
                    FieldError fieldError = (FieldError) error;
                    return fieldError.getField() + " : " + fieldError.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), erros);

    }

    public String getError() {
        return error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
