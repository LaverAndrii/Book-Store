package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateItemsQuantityDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String email);

    ShoppingCartDto addBook(AddCartItemDto addCartItemDto, String email);

    ShoppingCartDto updateCartItem(Long id, UpdateItemsQuantityDto updateQuantityDto, String email);

    void removeBook(Long id, String email);
}
