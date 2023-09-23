package slash.financing.controller;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.hasSize;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import slash.financing.data.User;
import slash.financing.dto.User.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.service.UserService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testGetAll_WithAdminUser() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().id(UUID.randomUUID()).email("user1@example.com").name("User 1").build(),
                User.builder().id(UUID.randomUUID()).email("user2@example.com").name("User 2").build()
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].email").value("user2@example.com"))
                .andExpect(jsonPath("$[1].name").value("User 2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testGetAll_WithRegularUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testGetOne_WithAdminUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User adminUser = User.builder()
                .id(userId)
                .email("admin@example.com")
                .name("Admin User")
                .role(UserRole.ADMIN)
                .build();
        User userToGet = User.builder()
                .id(userId)
                .email("user@example.com")
                .name("Regular User")
                .role(UserRole.USER)
                .build();

        when(userService.getUserByEmail(adminUser.getEmail())).thenReturn(adminUser);
        when(userService.getUserById(userId)).thenReturn(userToGet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Regular User"));

        verify(userService, times(1)).getUserById(userId);
    }


    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testGetOne_WithRegularUser_SameUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User userToGet = User.builder()
                .id(userId)
                .email("user@example.com")
                .name("Regular User")
                .role(UserRole.USER)
                .build();

        when(userService.getUserByEmail(userToGet.getEmail())).thenReturn(userToGet);
        when(userService.getUserById(userId)).thenReturn(userToGet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Regular User"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testGetOne_WithRegularUser_DifferentUser() throws Exception {
        User loggedInUser = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .name("Logged in User")
                .role(UserRole.USER)
                .build();
        User userToGet = User.builder()
                .id(UUID.randomUUID())
                .email("user123@example.com")
                .name("Regular User")
                .role(UserRole.USER)
                .build();

        when(userService.getUserByEmail(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(userService.getUserById(userToGet.getId())).thenReturn(userToGet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{uuid}", userToGet.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user123@example.com"))
                .andExpect(jsonPath("$.name").value("Regular User"));

        verify(userService, times(1)).getUserById(userToGet.getId());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testUpdateUser_RegularUser_SameUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder()
                .id(userId)
                .email("user@example.com")
                .name("Regular User")
                .build();

        when(userService.getUserById(userId)).thenReturn(regularUser);
        when(userService.updateUser(any(UserUpdateDto.class))).thenReturn(regularUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Regular User\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Regular User"));

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(any(UserUpdateDto.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testUpdateUser_RegularUser_DifferentUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").name("Regular User").build();
        User userToUpdate = User.builder().id(UUID.randomUUID()).email("other@example.com").name("Other User").build();

        when(userService.getUserById(userId)).thenReturn(userToUpdate);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Name\"}"))
                .andExpect(status().isForbidden());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    @Disabled
    public void testDeleteUser_RegularUser_SameUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder()
                .id(userId)
                .email("user2@example.com")
                .name("Regular User")
                .build();

        when(userService.getUserById(userId)).thenReturn(regularUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{uuid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User was successfully deleted"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    @Disabled
    public void testDeleteUser_RegularUser_DifferentUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").name("Regular User").build();
        User userToDelete = User.builder().id(UUID.randomUUID()).email("other@example.com").name("Other User").build();

        when(userService.getUserById(userId)).thenReturn(userToDelete);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }
}


