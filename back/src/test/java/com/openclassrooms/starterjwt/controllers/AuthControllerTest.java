package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
public class AuthControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    public class loginUserTests{
        @Test
        public void authenticateUserIsAdminTest() throws Exception {

            String email = "yoga@studio.com";

            LoginRequest loginRequest = new LoginRequest();

            loginRequest.setEmail(email);
            loginRequest.setPassword("Mypassword8$");

            mock.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        JwtResponse user = objectMapper.readValue(result, JwtResponse.class);
                        assertEquals(email, user.getUsername());
                    });

        }
        @Test
        public void authenticateUserNotAdminTest() throws Exception {

            String email = "test@studio.com";

            LoginRequest loginRequest = new LoginRequest();

            loginRequest.setEmail(email);
            loginRequest.setPassword("True123456");

            mock.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andDo(r -> {
                        String result = r.getResponse().getContentAsString();
                        JwtResponse user = objectMapper.readValue(result, JwtResponse.class);
                        assertEquals(email, user.getUsername());
                    });

        }
    }

    @Nested
    public class registerUserTests {
        @Test
        void shouldRegister() throws Exception {
            String email = "yoga2@studio.com";

            //GIVEN
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail(email);
            signupRequest.setFirstName("Hello");
            signupRequest.setLastName("World");
            signupRequest.setPassword("True123456");

            //WHEN & THEN
            mock.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldNotRegister() throws Exception {
            //GIVEN
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail(null);
            signupRequest.setFirstName("Hello");
            signupRequest.setLastName("World");
            signupRequest.setPassword("False123456");

            mock.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signupRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailToRegister() throws Exception {

            String email = "yoga@studio.com";

            SignupRequest signupRequest = new SignupRequest();

            signupRequest.setEmail(email);
            signupRequest.setFirstName("Hello");
            signupRequest.setLastName("World");
            signupRequest.setPassword("False123456");

            mock.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signupRequest)))
                    .andExpect(status().isBadRequest());
        }
    }


}