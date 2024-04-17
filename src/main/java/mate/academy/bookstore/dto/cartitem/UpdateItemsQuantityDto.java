package mate.academy.bookstore.dto.cartitem;

import jakarta.validation.constraints.Min;

public record UpdateItemsQuantityDto(@Min(1) int quantity) {
}
