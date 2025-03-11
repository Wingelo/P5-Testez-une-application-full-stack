package com.openclassrooms.starterjwt.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
public class UserControllerTest {
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
        void shouldHaveId() throws Exception {
            mock.perform(get("/api/user/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        UserDto userDto = objectMapper.readValue(result, UserDto.class);
                        assertNotNull(userDto);
                        assertEquals("Admin", userDto.getFirstName());
                    });
        }
        @Test
        @WithMockUser
        void shouldNotFound() throws Exception {
            mock.perform(get("/api/user/42")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void shouldIdNotNumber() throws Exception {
            mock.perform(get("/api/user/moi")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }


        @Nested
        public class deleteUser{

            @BeforeEach
            void setUp() throws Exception {
                LoginRequest loginRequest= new LoginRequest();
                loginRequest.setEmail("gym@studio.com");
                loginRequest.setPassword("Mypassword8$");
                restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
            }

            @Test
            void shouldDeleteNotFound() throws Exception {
                mock.perform(delete("/api/user/99999")
                                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isNotFound());
            }


            @Test
            void shouldUser() throws Exception {
                mock.perform(delete("/api/user/2")
                            .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isOk());
            }



            @Test
            void shouldUnauthorized() throws Exception {
                mock.perform(delete("/api/user/1")
                                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            void badRequest() throws Exception {
                mock.perform(delete("/api/user/test")
                                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isBadRequest());
            }

        }
    }
}
