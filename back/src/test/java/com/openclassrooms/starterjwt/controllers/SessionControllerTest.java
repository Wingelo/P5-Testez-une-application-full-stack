package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
public class SessionControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    public class FindById{

        @Test
        @WithMockUser
        public void shouldHaveId() throws Exception {
            mock.perform(get("/api/session/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        SessionDto sessionDto = objectMapper.readValue(result, SessionDto.class);
                        assertNotNull(sessionDto);
                        assertEquals("Yoga", sessionDto.getName());
                    });

        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {
            mock.perform(get("/api/session/42")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void shouldIdNotNumber() throws Exception {
            mock.perform(get("/api/session/moi")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }
    @Test
    @WithMockUser
    void findAllTest() throws Exception {
        mock.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(r -> {
                    String result = r.getResponse().getContentAsString();
                    List<SessionDto> sessionDto = objectMapper.readValue(result, new TypeReference<List<SessionDto>>() {});
                    assertNotNull(sessionDto);
                    assertEquals(2, sessionDto.size());
                });
    }

    @Test
    @WithMockUser
    void createSessionTest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Musculation");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Pour avoir des gros muscles");
        sessionDto.setTeacher_id(1L);

        mock.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andDo(r -> {
                    String result = r.getResponse().getContentAsString();
                    SessionDto outputSession = objectMapper.readValue(result, SessionDto.class);
                    assertEquals(sessionDto.getName(), outputSession.getName());
                });
    }

    @Nested
    public class UpdateTest {

        @Test
        @WithMockUser
        void shouldUpdateSessionTest() throws Exception {
           SessionDto sessionDto = new SessionDto();
           sessionDto.setName("Musculation");
           sessionDto.setDate(new Date());
           sessionDto.setDescription("Pour avoir des gros muscles");
           sessionDto.setTeacher_id(1L);
           sessionDto.setUpdatedAt(LocalDateTime.now());

           mock.perform(put("/api/session/1")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(sessionDto)))
                   .andExpect(status().isOk())
                   .andDo(r -> {
                       String result = r.getResponse().getContentAsString();
                       SessionDto outputSession = objectMapper.readValue(result, SessionDto.class);
                       assertEquals(sessionDto.getName(), outputSession.getName());
                   });

       }

        @Test
        @WithMockUser
        void shouldIdNotNumber() throws Exception {
            mock.perform(put("/api/session/moi")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    public class DeleteTest {

        @Test
        @WithMockUser
        void shouldSave() throws Exception {
            mock.perform(delete("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {
            mock.perform(delete("/api/session/42")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void shouldIdNotNumber() throws Exception {
            mock.perform(delete("/api/session/moi")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    public class ParticipateSessionTest {

        @Test
        @WithMockUser
        void shouldParticipateSessionTest() throws Exception {
            Long sessionId = 2L;
            Long userId = 1L;

            mock.perform(post("/api/session/" + sessionId + "/participate/" + userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mock.perform(get("/api/session/" + sessionId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        SessionDto sessionDto = objectMapper.readValue(result, SessionDto.class);
                        assertEquals(userId, sessionDto.getUsers().get(0));
                    });

        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            mock.perform(post("/api/session/" + "invalid_number" + "/participate/" + "invalid_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mock.perform(get("/api/session/" + "invalid_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
    @Nested
    public class noLongerParticipateTest {

        @Test
        @WithMockUser
        void shouldNotParticipate() throws Exception {


            Long sessionId = 1L;
            Long userId = 1L;

            mock.perform(delete("/api/session/" + sessionId + "/participate/" + userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mock.perform(get("/api/session/" + sessionId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        SessionDto outputSession = objectMapper.readValue(result, SessionDto.class);
                        assertEquals(0, outputSession.getUsers().size());
                    });

        }

        @Test
        @WithMockUser
        void shouldBadRequest() throws Exception {

            mock.perform(delete("/api/session/" + "invalid_number" + "/participate/" + "invalid_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mock.perform(get("/api/session/" + "invalid_number")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }
    }
}
