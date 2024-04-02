package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mate.academy.bookstore.validation.Email;

public record UserLoginRequestDto(
        @NotBlank(message = "Email should be valid")
        @Size(max = 20)
        @Email
        String email,
        @NotBlank(message = "Password should be valid")
        @Size(min = 4, max = 20)
        String password) {
}
