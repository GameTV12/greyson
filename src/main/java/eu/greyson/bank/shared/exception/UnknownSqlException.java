package eu.greyson.bank.shared.exception;

/**
 * Throws when unknown SQL error is received.
 */
public class UnknownSqlException extends RuntimeException {
    public UnknownSqlException(String message) { super(message); }
}