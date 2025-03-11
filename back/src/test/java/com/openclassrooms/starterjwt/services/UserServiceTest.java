package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void deleteTest(){
        //GIVEN
        Long userId = 1L;
        //WHEN
        userService.delete(userId);
        //THEN
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findByIdTest(){
        //GIVEN
        Long userId = 1L;
        //WHEN
        userService.findById(userId);
        //THEN
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findByNoIdUserTest(){
        //GIVEN
        Long userId = 99L;
        //WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        User result = userService.findById(userId);
        //THEN
        assertThat(result).isNull();
        verify(userRepository, times(1)).findById(userId);
    }
}
