package com.peterservice.zman.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SERVERS")
public class ZServer {
    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Alias may not be empty")
    @Column(unique = true)
    private String alias;
    @NotNull(message = "Connection string may not be null")
    private String connectionString;
    private String login;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZServer zServer = (ZServer) o;

        if (id != null ? !id.equals(zServer.id) : zServer.id != null) return false;
        if (alias != null ? !alias.equals(zServer.alias) : zServer.alias != null) return false;
        if (connectionString != null ? !connectionString.equals(zServer.connectionString) : zServer.connectionString != null)
            return false;
        if (login != null ? !login.equals(zServer.login) : zServer.login != null) return false;
        return password != null ? password.equals(zServer.password) : zServer.password == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (connectionString != null ? connectionString.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
