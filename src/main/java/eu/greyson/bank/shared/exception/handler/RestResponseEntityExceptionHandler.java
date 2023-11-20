package eu.greyson.bank.shared.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import eu.greyson.bank.shared.dto.ExceptionDto;
import eu.greyson.bank.shared.dto.ValidationErrorDto;
import eu.greyson.bank.shared.exception.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This handler catch all exceptions, which will arise in app and translate that errors into
 * {@link ExceptionDto}.
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ExceptionDto> handleError(ValidationException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTypeIdException.class)
    protected ResponseEntity<ExceptionDto> handleError(InvalidTypeIdException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ExceptionDto> handleError(HttpMessageNotReadableException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    protected ResponseEntity<ExceptionDto> handleError(ServletRequestBindingException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleError(BadRequestException ex) {
        return handleError("Bad request", ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handleError(ResourceNotFoundException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ExceptionDto> handleError(EntityNotFoundException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChangeStateNotAllowedException.class)
    protected ResponseEntity<ExceptionDto> handleError(ChangeStateNotAllowedException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConfigurationException.class)
    protected ResponseEntity<ExceptionDto> handleError(ConfigurationException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalErrorException.class)
    protected ResponseEntity<ExceptionDto> handleError(InternalErrorException ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionDto> handleError(Exception ex) {
        return handleError(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ExceptionDto> handleError(BindException ex) {
        return handleValidationError(ex, ex.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionDto> handleError(MethodArgumentNotValidException ex) {
        return handleValidationError(ex, ex.getBindingResult());
    }

    private ResponseEntity<ExceptionDto> handleError(String message, Throwable e, HttpStatus status) {
        logger.error(message, e);
        return createResponseEntity(message, e, status);
    }

    private ResponseEntity<ExceptionDto> handleValidationError(Exception ex, BindingResult bindingResult) {
        String errorMessage = "Validation error";

        var errors = bindingResult
                .getFieldErrors()
                .stream()
                .map(ValidationErrorDto::from)
                .collect(Collectors.toList());

        logger.warn(errorMessage, ex);

        return createResponseEntity(errorMessage, errors);
    }

    private ResponseEntity<ExceptionDto> createResponseEntity(String errorMessage, Throwable e, HttpStatus status) {
        return new ResponseEntity<>(
                new ExceptionDto(
                        status.value(),
                        errorMessage,
                        ExceptionUtils.getStackTrace(e)
                ),
                status);
    }

    private ResponseEntity<ExceptionDto> createResponseEntity(String errorMessage,
                                                              List<ValidationErrorDto> validationErrors) {
        return new ResponseEntity<>(
                new ExceptionDto(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage,
                        validationErrors
                ),
                HttpStatus.BAD_REQUEST);
    }
}
