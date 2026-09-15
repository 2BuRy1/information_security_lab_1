package com.security.lab.configuration;

import com.security.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthenticationConfiguration {

    public static final String DAO_AUTHENTICATION_PROVIDER = "DaoAuthenticationProvider";
    public static final String SECURITY_LAB_USER_DETAILS_SERVICE = "SecurityLabUserDetailsService";

    private final UserRepository userRepository;

    public AuthenticationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean(SECURITY_LAB_USER_DETAILS_SERVICE)
    public UserDetailsService userDetailsService() {
        return username ->
                userRepository
                        .findUsersByLogin(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(DAO_AUTHENTICATION_PROVIDER)
    public AuthenticationProvider authenticationProvider(
            @Qualifier(SECURITY_LAB_USER_DETAILS_SERVICE) UserDetailsService userDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            @Qualifier(DAO_AUTHENTICATION_PROVIDER)
                    AuthenticationProvider daoAuthenticationProvider) {
        return daoAuthenticationProvider::authenticate;
    }
}
