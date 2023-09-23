package slash.financing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @Length(min = 3, max = 35, message = "username must be between 3 and 35")
    @NotNull
    private String username;

    @Email(message = "email must be valid")
    @NotNull
    private String email;

    @Length(min = 8, max = 25, message = "password must be between 8 and 25")
    @NotNull
    private String password;
}