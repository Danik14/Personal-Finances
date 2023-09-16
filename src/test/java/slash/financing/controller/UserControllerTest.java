package slash.financing.controller;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.hasSize;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import slash.financing.data.User;
import slash.financing.dto.User.UserDto;
import slash.financing.dto.User.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.service.UserService;

import java.security.Principal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    public void testGetAll_WithAdminUser() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().id(UUID.randomUUID()).email("user1@example.com").username("User 1").build(),
                User.builder().id(UUID.randomUUID()).email("user2@example.com").username("User 2").build()
        );

        when(userService.getAllUsers()).thenReturn(users);

        List<UserDto> userDtos = Arrays.asList(
                UserDto.builder().id(UUID.randomUUID()).email("user1@example.com").username("User 1").build(),
                UserDto.builder().id(UUID.randomUUID()).email("user2@example.com").username("User 2").build()
        );

        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDtos.get(0), userDtos.get(1));

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
        verify(modelMapper, times(2)).map(any(User.class), eq(UserDto.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testGetAll_WithRegularUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verifyNoInteractions(userService, modelMapper);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ROLE_ADMIN"})
    @Disabled
    public void testGetOne_WithAdminUser() throws Exception {
        Principal principal = () -> "admin@example.com";
        UUID userId = UUID.randomUUID();
        User adminUser = User.builder()
                .id(userId)
                .email("admin@example.com")
                .username("Admin User")
                .role(UserRole.ADMIN)
                .build();
        User userToGet = User.builder()
                .id(userId)
                .email("user@example.com")
                .username("Regular User")
                .role(UserRole.USER)
                .build();

        when(userService.getUserByEmail(adminUser.getEmail())).thenReturn(adminUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{uuid}", userId.toString())
                        .principal(principal)
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
        User regularUser = User.builder().id(userId).email("user@example.com").username("Regular User").build();

        when(userService.getUserById(userId)).thenReturn(regularUser);

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
    public void testGetOne_WithRegularUser_DifferentUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").username("Regular User").build();
        User userToGet = User.builder().id(UUID.randomUUID()).email("other@example.com").username("Other User").build();

        when(userService.getUserById(userId)).thenReturn(userToGet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()) // Limited user information
                .andExpect(jsonPath("$.email").value("other@example.com"))
                .andExpect(jsonPath("$.name").value("Other User"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    public void testUpdateUser_AdminUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User adminUser = User.builder().id(userId).email("admin@example.com").username("Admin User").build();

        when(userService.getUserById(userId)).thenReturn(adminUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Name\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.name").value("New Name"));

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(eq(adminUser), any(UserUpdateDto.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testUpdateUser_RegularUser_SameUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").username("Regular User").build();

        when(userService.getUserById(userId)).thenReturn(regularUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Name\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("New Name"));

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(eq(regularUser), any(UserUpdateDto.class));
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testUpdateUser_RegularUser_DifferentUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").username("Regular User").build();
        User userToUpdate = User.builder().id(UUID.randomUUID()).email("other@example.com").username("Other User").build();

        when(userService.getUserById(userId)).thenReturn(userToUpdate);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Name\"}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"USER"})
    public void testDeleteUser_RegularUser_SameUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder()
                .id(userId)
                .email("user2@example.com")
                .username("Regular User")
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
    public void testDeleteUser_RegularUser_DifferentUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User regularUser = User.builder().id(userId).email("user@example.com").username("Regular User").build();
        User userToDelete = User.builder().id(UUID.randomUUID()).email("other@example.com").username("Other User").build();

        when(userService.getUserById(userId)).thenReturn(userToDelete);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{uuid}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }
}


