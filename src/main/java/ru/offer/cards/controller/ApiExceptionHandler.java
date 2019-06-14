package ru.offer.cards.controller;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.offer.cards.exception.ApiException;

import static ru.offer.cards.config.JsonApiConfig.JSON_API_MEDIA_TYPE;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResourceConverter resourceConverter;

    @Autowired
    public ApiExceptionHandler(final ResourceConverter resourceConverter) {
        this.resourceConverter = resourceConverter;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(RuntimeException ex, WebRequest request) throws Exception {
        if (ex instanceof ApiException) {
            val localEx = (ApiException) ex;
            return ResponseEntity
                    .status(localEx.getHttpStatus())
                    .contentType(JSON_API_MEDIA_TYPE)
                    .body(resourceConverter.writeDocument(new JSONAPIDocument<>(localEx.getErrors())));
        } else {
            throw ex;
        }
    }

}
