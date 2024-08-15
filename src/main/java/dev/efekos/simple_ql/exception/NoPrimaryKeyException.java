package dev.efekos.simple_ql.exception;

public class NoPrimaryKeyException extends RuntimeException {
    public NoPrimaryKeyException(String message) {
        super(message);
    }
}
