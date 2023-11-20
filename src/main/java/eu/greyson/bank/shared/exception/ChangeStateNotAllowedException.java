package eu.greyson.bank.shared.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ChangeStateNotAllowedException extends RuntimeException {

    public ChangeStateNotAllowedException(String msg) {
        super(msg);
    }
}
