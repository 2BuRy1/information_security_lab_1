package com.security.lab.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id @GeneratedValue private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    // "user" is a reserved word in PostgreSQL, hence "users"/"users_authorities"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority", nullable = false)
    private List<String> authorities;

    public User(String login, String password, List<GrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }

    public User(String login, String password) {
        this(login, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    protected User() {
        // required by Hibernate
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }
}
