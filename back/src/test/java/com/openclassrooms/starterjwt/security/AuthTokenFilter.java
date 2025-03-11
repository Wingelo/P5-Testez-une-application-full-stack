package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthTokenFilterTest {

    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

    @Test
    void parseJwtValidToken(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        String validToken = "validToken";
        request.addHeader("Authorization", "Bearer " + validToken);

        // WHEN
        String result = authTokenFilter.parseJwt(request);

        // THEN
        assertThat(result).isEqualTo(validToken);
    }

    @Test
    void parseJwtNoTokenInHeader() {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();

        // WHEN
        String result = authTokenFilter.parseJwt(request);

        // THEN
        assertThat(result).isNull();
    }
}
