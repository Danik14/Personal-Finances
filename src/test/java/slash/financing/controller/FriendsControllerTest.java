package slash.financing.controller;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private FriendsController friendsController;

    @Mock
    private UserService userService;

    @Mock
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendsController).build();
        reset(userService);
    }

    @Test
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
    public void testAddFriend_ValidUser() throws Exception {
        Principal principal = () -> "user@example.com";

        User user = new User();
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        User friend = User.builder()
                .id(UUID.randomUUID())
                .username("Ryan Gosling")
                .build();
        when(userService.getUserById(any(UUID.class))).thenReturn(friend);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/friends/{uuid}", UUID.randomUUID().toString())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("Ryan Gosling"));

        verify(userService, times(1)).getUserByEmail(anyString());
        verify(userService, times(1)).getUserById(any(UUID.class));
        verify(friendService, times(1)).addFriendToUser(any(User.class), any(User.class));
    }

    @Test
    public void testAddFriend_InvalidUUID() throws Exception {
        Principal principal = () -> "user@example.com";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/friends/invalid-uuid")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, friendService);
    }

    @Test
    public void testDeleteUser_ValidUUID() throws Exception {
        Principal principal = () -> "user@example.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/friends/{uuid}", UUID.randomUUID().toString())
                        .principal(principal))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(any(UUID.class));
    }

    @Test
    @Disabled
    public void testDeleteUser_InvalidUUID() throws Exception {
        Principal principal = () -> "user@example.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/friends/invalid-uuid")
                        .principal(principal))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}
