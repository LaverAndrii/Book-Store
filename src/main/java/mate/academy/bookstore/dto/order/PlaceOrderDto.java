package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderDto(
        @NotBlank(message = "may not be blank") String shippingAddress) {
}
