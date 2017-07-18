package com.hashtagplus.model;

import com.mongodb.annotations.Immutable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Immutable
@Document(collection = "users")
public class User {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLES = "roles";

    @Id
    public String id;

    private String username;

    private String password;

    private List<GrantedAuthority> roles;

    public User() {}

    public User(String username, String password, List<GrantedAuthority> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[userName='%s']", username);
    }

}
