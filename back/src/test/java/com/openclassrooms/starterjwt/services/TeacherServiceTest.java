package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void findAllTest(){
        List<Teacher> teachers = List.of(
                new Teacher(1L,"WORLD", "Hello", LocalDateTime.now(), LocalDateTime.now()),
                new Teacher(2L,"CENA", "John", LocalDateTime.now(), LocalDateTime.now())
        );
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertThat(result).isEqualTo(teachers);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findOneIdTeacherTest(){
        Long teacherId = 1L;
        Teacher teacher = new Teacher(1L,"WORLD", "Hello", LocalDateTime.now(), LocalDateTime.now());
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(teacherId);

        assertThat(result).isEqualTo(teacher);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void findByNoIdUserTest(){
        Long teacherId = 99L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        Teacher result = teacherService.findById(teacherId);

        assertThat(result).isNull();
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
