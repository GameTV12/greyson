package eu.greyson.bank.shared.exception;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus
public class InternalErrorException extends RuntimeException {

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

