package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem save(AddCartItemDto addCartItemDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toEntity(addCartItemDto);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }
}
