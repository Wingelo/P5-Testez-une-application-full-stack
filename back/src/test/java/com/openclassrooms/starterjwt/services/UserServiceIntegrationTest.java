package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void deleteTest(){
        //GIVEN
        Long userId = 1L;

        //WHEN
        userService.delete(userId);

        //THEN
        verify(userRepository).deleteById(userId);
    }

    @Nested
    public class findByIdTest{

        @Test
        public void shouldFind(){
            //GIVEN
            User user = new User();
            user.setId(1L);
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("john@doe.com");
            user.setPassword("password");

            //WHEN
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            //THEN
            User result = userService.findById(1L);
            assertEquals(user, result);
        }

        @Test
        public void shouldNotFind(){
            //WHEN
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            //THEN
            User result = userService.findById(1L);
            assertNull(result);
        }
    }
}
