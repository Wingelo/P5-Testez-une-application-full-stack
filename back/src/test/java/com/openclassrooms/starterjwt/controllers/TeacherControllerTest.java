package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
public class TeacherControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    public class FindById {

        @Test
        @WithMockUser
        void shouldHaveId() throws Exception {
            mock.perform(get("/api/teacher/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        TeacherDto teacherDto = objectMapper.readValue(result, TeacherDto.class);
                        assertNotNull(teacherDto);
                        assertEquals("Margot", teacherDto.getFirstName());
                    });
        }
        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {
            mock.perform(get("/api/teacher/42")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void shouldIdNotNumber() throws Exception {
            mock.perform(get("/api/teacher/moi")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }


    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        mock.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(r -> {
                    String result = r.getResponse().getContentAsString();
                    List<TeacherDto> teacherDtoList = objectMapper.readValue(result, new TypeReference<List<TeacherDto>>() {});
                    assertNotNull(teacherDtoList);
                    assertEquals(2, teacherDtoList.size());
                });
    }

}
