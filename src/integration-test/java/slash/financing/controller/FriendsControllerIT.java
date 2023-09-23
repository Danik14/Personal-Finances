package slash.financing.controller;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import slash.financing.data.User;
import slash.financing.service.FriendService;
import slash.financing.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FriendsControllerIT {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private FriendsController friendsController;

    @MockBean
    private UserService userService;

    @MockBean
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
        reset(userService);
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testGetAllFriends_ValidUser() throws Exception {
        Principal principal = () -> "user@example.com";

        List<User> friends = new ArrayList<>();
        User friend1 = new User();
        User friend2 = new User();
        friends.add(friend1);
        friends.add(friend2);

        when(userService.getUserByEmail(anyString())).thenReturn(new User());
        when(userService.getUserByEmail("user@example.com")).thenReturn(User.builder().friends(friends).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/friends")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(userService, times(1)).getUserByEmail(anyString());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testAddFriend_ValidUser() throws Exception {
        Principal principal = () -> "user@example.com";

        User user = new User();
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        User friend = User.builder()
                .id(UUID.randomUUID())
                .name("Ryan Gosling")
                .build();
        when(userService.getUserById(any(UUID.class))).thenReturn(friend);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/friends/{uuid}", UUID.randomUUID().toString())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ryan Gosling"));

        verify(userService, times(1)).getUserByEmail(anyString());
        verify(userService, times(1)).getUserById(any(UUID.class));
        verify(friendService, times(1)).addFriendToUser(any(User.class), any(User.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testAddFriend_InvalidUUID() throws Exception {
        Principal principal = () -> "user@example.com";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/friends/invalid-uuid")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, friendService);
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    @Disabled
    public void testDeleteUser_ValidUUID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/api/v1/users/friends/{uuid}",
                                UUID.randomUUID().toString()))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(any(UUID.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    @Disabled
    public void testDeleteUser_InvalidUUID() throws Exception {
        Principal principal = () -> "user@example.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/friends/invalid-uuid")
                        .principal(principal))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}
