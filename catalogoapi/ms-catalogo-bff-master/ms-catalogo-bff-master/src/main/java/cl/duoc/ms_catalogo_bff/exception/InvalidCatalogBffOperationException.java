package cl.duoc.ms_catalogo_bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCatalogBffOperationException extends RuntimeException {
    public InvalidCatalogBffOperationException(String message) {
        super(message);
    }
}

