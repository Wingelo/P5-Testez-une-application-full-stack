package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void loadUserByUsername() {
        //GIVEN
        User user = new User(1L, "john.doe@example.com", "Doe", "John", "JaimeLeCode", false, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(java.util.Optional.of(user));

        //WHEN
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername("john.doe@example.com");

        //THEN
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDetails.getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("john.doe@example.com"))
        .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: john.doe@example.com");
    }
}
