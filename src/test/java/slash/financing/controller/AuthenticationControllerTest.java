package slash.financing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import slash.financing.dto.AuthenticationRequestDto;
import slash.financing.dto.AuthenticationResponse;
import slash.financing.dto.RegistrationRequest;
import slash.financing.service.AuthenticationService;
import slash.financing.service.VerificationTokenService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON serialization/deserialization

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private VerificationTokenService verificationTokenService;

    @BeforeEach
    void setUp() {
        // Reset any mock behavior before each test
        reset(authenticationService);
        reset(verificationTokenService);
    }

    @Test
    public void testRegisterValidRequest() throws Exception {
        // Create a valid registration request
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");

        // Mock the registration service response
        AuthenticationResponse mockResponse = new AuthenticationResponse("token123");
        when(authenticationService.register(any(RegistrationRequest.class))).thenReturn(mockResponse);

        // Perform the registration request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token123"));

        // Verify that the registration service was called with the correct request
        verify(authenticationService, times(1)).register(eq(registrationRequest));
    }

    @Test
    public void testRegisterMissingRequestBody() throws Exception {
        // Perform a registration request with missing request body
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request body is missing"));
    }


    @Test
    @WithMockUser
    public void testAuthenticateValidRequest() throws Exception {
        // Create a valid authentication request
        AuthenticationRequestDto authenticationRequest = new AuthenticationRequestDto();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password123");

        // Mock the authentication service response
        AuthenticationResponse mockResponse = new AuthenticationResponse("token123");
        when(authenticationService.authenticate(any(AuthenticationRequestDto.class))).thenReturn(mockResponse);

        // Perform the authentication request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token123"));

        // Verify that the authentication service was called with the correct request
        verify(authenticationService, times(1)).authenticate(eq(authenticationRequest));
    }

    @Test
    public void testAuthenticateMissingRequestBody() throws Exception {
        // Perform an authentication request with missing request body
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request body is missing"));
    }


    @Test
    public void testVerify() throws Exception {
        // Mock the token verification service response
        when(verificationTokenService.verifyToken("token123")).thenReturn("Verification successful");

        // Perform the verification request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/verify")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Verification successful"));

        // Verify that the token verification service was called with the correct token
        verify(verificationTokenService, times(1)).verifyToken("token123");
    }
}
