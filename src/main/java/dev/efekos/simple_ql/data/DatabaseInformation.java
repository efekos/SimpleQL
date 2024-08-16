package dev.efekos.simple_ql.data;

import javax.xml.crypto.Data;
import java.util.Objects;

public class DatabaseInformation {

    private String connectionUrl;
    private String username;
    private String password;
    private String databaseName = "simple_ql";

    public DatabaseInformation(String connectionUrl, String username, String password, String databaseName) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    public DatabaseInformation(String connectionUrl, String username, String password) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatabaseInformation{" +
                "connectionUrl='" + connectionUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }

    public DatabaseType getType(){
        for (DatabaseType type : DatabaseType.values())
            if(connectionUrl.startsWith("jdbc:"+type.getName()))return type;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseInformation that = (DatabaseInformation) o;
        return Objects.equals(getConnectionUrl(), that.getConnectionUrl()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(databaseName, that.databaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConnectionUrl(), getUsername(), getPassword(), databaseName);
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
