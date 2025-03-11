package com.openclassrooms.starterjwt.security;

import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void initialize() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("JaimeLeCode")
                .build();

    }

    @Test
    void testGetAuthorities() {
        //WHEN
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        //THEN
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    void testIsAccountNonExpired() {
        //THEN
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsAccountNonLocked() {
        //THEN
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        //THEN
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void testEqualsSameObject() {
        assertThat(userDetails.equals(userDetails)).isTrue();
    }

    @Test
    void testEqualIsDifferentClass() {
        assertThat(userDetails.equals(new Object())).isFalse();
    }

    @Test
    void testEqualSameId(){
        assertThat(userDetails.equals(userDetails)).isTrue();
    }
}
