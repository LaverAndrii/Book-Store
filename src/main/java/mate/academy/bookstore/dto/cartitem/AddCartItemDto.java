package mate.academy.bookstore.dto.cartitem;

import lombok.Data;

@Data
public class AddCartItemDto {
    private Long bookId;
    private int quantity;
}
