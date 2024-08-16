package dev.efekos.simple_ql.data;

public enum DatabaseType {
    MYSQL(true, "mysql"),
    SQLITE(false, "sqlite");

    private final boolean createSchema;
    private final String name;

    DatabaseType(boolean createSchema, String name) {
        this.createSchema = createSchema;
        this.name = name;
    }

    public boolean shouldCreateSchema() {
        return createSchema;
    }

    public String getName() {
        return name;
    }
}
