package com.uc.ms_security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "security.jwt.secret=test-only-jwt-secret-with-at-least-32-bytes")
@AutoConfigureMockMvc
class MsSecurityApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void protectedEndpointRejectsMissingAndInvalidTokens() throws Exception {
		mockMvc.perform(get("/api/users"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/api/users")
						.header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void loginEndpointIsPublicAndValidatesRequestBody() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType("application/json")
						.content("""
								{"email":"invalid-email","password":""}
								"""))
				.andExpect(status().isBadRequest());
	}
}
