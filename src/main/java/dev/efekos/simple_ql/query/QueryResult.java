package dev.efekos.simple_ql.query;

import java.util.List;

public record QueryResult<T>(Exception exception, List<T> result) {

    public boolean hasResult() {
        return result != null;
    }

    public boolean hasException() {
        return exception != null;
    }

}
