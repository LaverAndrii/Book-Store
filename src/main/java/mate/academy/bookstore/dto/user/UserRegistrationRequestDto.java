package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.bookstore.validation.Email;
import mate.academy.bookstore.validation.FieldMatch;

@FieldMatch(first = "password", second = "repeatPassword")
@Data
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
