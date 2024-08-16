package dev.efekos.simple_ql.exception;

import java.lang.reflect.Field;

public class NoGetterException extends RuntimeException {
    public NoGetterException(Field field) {
        super("No getter found for field " + field.toString()+". Please only use primitive types, String, UUID and subtypes of TableRowTypeAdapter");
    }
}
