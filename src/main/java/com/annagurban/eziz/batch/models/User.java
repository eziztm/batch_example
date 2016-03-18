package com.annagurban.eziz.batch.models;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String username;
    private Set<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        if (roles == null) {
            roles = new HashSet();
        }
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
