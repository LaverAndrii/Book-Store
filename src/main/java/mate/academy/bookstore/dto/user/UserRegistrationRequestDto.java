package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.bookstore.validation.Email;
import mate.academy.bookstore.validation.FieldMatch;

@FieldMatch(first = "password", second = "repeatPassword")
@Data
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    @Size(max = 20)
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
    @NotBlank
    @Size(min = 4, max = 20)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
