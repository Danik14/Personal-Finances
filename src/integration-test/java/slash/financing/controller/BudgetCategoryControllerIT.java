package slash.financing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import slash.financing.data.BudgetCategory;
import slash.financing.data.User;
import slash.financing.dto.BudgetCategoryDto;
import slash.financing.service.BudgetCategoryService;
import slash.financing.service.UserService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BudgetCategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BudgetCategoryService budgetCategoryService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        reset(budgetCategoryService);
        reset(userService);
    }

    @Test
    @WithMockUser // Simulate an authenticated user
    public void testAddPersonalBudgetCategory_ValidInput() throws Exception {
        // Create a valid BudgetCategoryDto for testing
        BudgetCategoryDto budgetCategoryDto = new BudgetCategoryDto();
        budgetCategoryDto.setName("Personal Category");
        budgetCategoryDto.setDescription("Valid description");

        // Mock the principal's name for the test
        when(userService.getUserByEmail(anyString())).thenReturn(new User());

        // Mock the service to return the created BudgetCategory
        BudgetCategory createdCategory = new BudgetCategory();
        createdCategory.setId(UUID.fromString("a5d31579-616b-445a-9e59-3a60b15f32d6"));
        createdCategory.setName("Personal Category");
        createdCategory.setDescription("Valid description");
        when(budgetCategoryService.createPersonalBudgetCategory(any(BudgetCategoryDto.class), any(User.class)))
                .thenReturn(createdCategory);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/budgetcategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCategoryDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("a5d31579-616b-445a-9e59-3a60b15f32d6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Personal Category"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Valid description"));

        // Verify that the service method was called with the correct arguments
        verify(budgetCategoryService, times(1)).createPersonalBudgetCategory(eq(budgetCategoryDto), any(User.class));
    }

    @Test
    @WithMockUser // Simulate an authenticated user
    public void testAddPersonalBudgetCategory_InvalidInput() throws Exception {
        BudgetCategoryDto budgetCategoryDto = new BudgetCategoryDto();
        budgetCategoryDto.setName("A"); // Does not meet the length constraint
        budgetCategoryDto.setDescription("Valid description");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/budgetcategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCategoryDto)))
                .andExpect(status().isBadRequest());

        // Verify that the service method was not called (invalid input should prevent the service call)
        verify(budgetCategoryService, never()).createPersonalBudgetCategory(eq(budgetCategoryDto), any(User.class));
    }

}
