package slash.financing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import slash.financing.data.BudgetCategory;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.dto.Expense.ExpenseDto;
import slash.financing.service.BudgetCategoryService;
import slash.financing.service.ExpenseService;
import slash.financing.service.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private BudgetCategoryService budgetCategoryService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testGetUserExpenses_ValidUser() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(new User());

        List<Expense> expenses = Arrays.asList(
                Expense.builder().id(UUID.randomUUID()).amount(BigDecimal.valueOf(100)).description("Expense 1").build(),
                Expense.builder().id(UUID.randomUUID()).amount(BigDecimal.valueOf(200)).description("Expense 2").build()
        );
        when(expenseService.getAllUserExpenses(any(User.class))).thenReturn(expenses);
        when(expenseService.getTotalMoneySpentForUser(any(User.class))).thenReturn(BigDecimal.valueOf(300));

        mockMvc.perform(get("/api/v1/expenses").principal(
                        () -> {
                            return "user@example.com";
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(300.0))
                .andExpect(jsonPath("$.expenses").isArray())
                .andExpect(jsonPath("$.expenses", hasSize(2)))
                .andExpect(jsonPath("$.expenses[0].description").value("Expense 1"))
                .andExpect(jsonPath("$.expenses[1].description").value("Expense 2"));

        verify(expenseService, times(1)).getAllUserExpenses(any(User.class));
    }

    @Test
    @WithMockUser
    public void testAddUserExpense_ValidInput() throws Exception {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(BigDecimal.valueOf(150));
        expenseDto.setDescription("Valid expense");

        when(userService.getUserByEmail(anyString())).thenReturn(new User());

        Expense createdExpense = Expense.builder()
                .id(UUID.randomUUID())
                .amount(expenseDto.getAmount())
                .description(expenseDto.getDescription())
                .build();
        when(expenseService.saveExpense(any(Expense.class))).thenReturn(createdExpense);
        when(budgetCategoryService.getBudgetCategoryById(any(UUID.class))).thenReturn(new BudgetCategory());

        mockMvc.perform(post("/api/v1/expenses/{budgetCategoryId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdExpense.getId().toString()))
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.description").value("Valid expense"));

        verify(expenseService, times(1)).saveExpense(any(Expense.class));
    }
}
