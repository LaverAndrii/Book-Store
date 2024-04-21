package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;

public interface CartItemService {
    CartItem save(AddCartItemDto addCartItemDto, ShoppingCart shoppingCart);

    CartItem updateQuantity(Long cartId, int quantity);

    CartItem getById(Long cartId);
}
