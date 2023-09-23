package slash.financing.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VerifiedUserCheckControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithMockUser(username = "verifiedUser@example.com", roles = {"VERIFIED_USER"})
    public void testAdminSomething_WithVerifiedUser() throws Exception {
        String userName = "Alice";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/verified/{name}", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, verified user " + userName));
    }

    @Test
    @WithMockUser(username = "unverifiedUser", roles = "USER")
    public void testAdminSomething_WithUnverifiedUser() throws Exception {
        String userName = "Bob";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/verified/{name}", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAdminSomething_WithoutUser() throws Exception {
        String userName = "Eve";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/verified/{name}", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}