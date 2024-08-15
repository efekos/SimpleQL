package dev.efekos.simple_ql.exception;

import java.lang.reflect.Field;

public class NoSetterException extends RuntimeException {
    public NoSetterException(Field field) {
        super("No setter found for field " + field.toString()+". Please only use primitive types, String, UUID and subtypes of TableRowTypeAdapter");
    }
}
