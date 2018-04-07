package org.java.rest.service.exception;

/**
 * Thrown to indicate any problems that encountered when transferring money
 */
public class TransferException extends Exception {

    public TransferException(String message) {
        super(message);
    }
}
