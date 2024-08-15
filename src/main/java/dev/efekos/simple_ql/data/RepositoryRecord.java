package dev.efekos.simple_ql.data;

public interface RepositoryRecord {

    boolean delete();
    void clean();
    boolean isDirty();

}
