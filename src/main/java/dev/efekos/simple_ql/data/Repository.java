package dev.efekos.simple_ql.data;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<K,T> {

    Optional<T> find(K id);
    Stream<T> findAll();
    boolean insert(T t);

}
