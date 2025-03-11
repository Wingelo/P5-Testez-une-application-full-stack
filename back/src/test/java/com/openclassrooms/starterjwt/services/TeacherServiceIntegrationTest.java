package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeacherServiceIntegrationTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Test
    public void findAllTest(){
        //Creation of test data
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("CENA");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("WILLIAMS");

        List<Teacher> teachers = Arrays.asList(teacher, teacher2);

        // Define mock behavior
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Call the method to test
        List<Teacher> result = teacherService.findAll();

        // Check the result
        assertEquals(teachers, result);

    }

    @Test
    public void findByIdTest(){

        // Creation of test data
        Teacher teacher = new Teacher();
        teacher.setId(99L);
        teacher.setFirstName("John");
        teacher.setLastName("CENA");

        // Define mock behavior
        when(teacherRepository.findById(99L)).thenReturn(Optional.of(teacher));
        // Call the method to test
        Teacher result = teacherService.findById(99L);
        // Check the result
        assertEquals(teacher, result);
    }
}
