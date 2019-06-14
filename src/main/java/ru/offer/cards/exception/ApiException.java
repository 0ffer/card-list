package ru.offer.cards.exception;

import com.github.jasminb.jsonapi.models.errors.Error;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Exception for json api errors.
 *
 * @author Stas Melnichuk
 */
@Data
public class ApiException extends RuntimeException {
    private HttpStatus httpStatus;
    private List<Error> errors = new ArrayList<>();

    public ApiException(final HttpStatus httpStatus, final List<Error> errors) {
        this.httpStatus = httpStatus;
        this.errors.addAll(errors);
    }

    public ApiException(final HttpStatus httpStatus, final Error error) {
        this.httpStatus = httpStatus;
        this.errors.add(error);
    }

    public void addError(final Error error) {
        this.errors.add(error);
    }
}
