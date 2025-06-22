package cl.duoc.ms_catalogo_bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CatalogBffNotFoundException extends RuntimeException {
    public CatalogBffNotFoundException(String message) {
        super(message);
    }
}

