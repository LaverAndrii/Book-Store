package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateItemsQuantityDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto getShoppingCart(String email) {
        return shoppingCartMapper.toDto(getShoppingCartByEmail(email));
    }

    @Override
    public ShoppingCartDto addBook(AddCartItemDto addCartItemDto, String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = cartItemService.save(addCartItemDto, shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto updateCartItem(
            Long cartId,
            UpdateItemsQuantityDto updateQuantityDto,
            String email) {
        if (!checkItemUser(cartId, email)) {
            throw new EntityNotFoundException("There is no such item in the shopping cart");
        }
        cartItemService.updateQuantity(cartId, updateQuantityDto.quantity());
        return shoppingCartMapper.toDto(getShoppingCartByEmail(email));
    }

    @Override
    public ShoppingCartDto removeBook(Long cartId, String email) {
        if (!checkItemUser(cartId, email)) {
            throw new EntityNotFoundException("There is no such item in the shopping cart");
        }
        cartItemRepository.deleteById(cartId);
        return getShoppingCart(email);
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        return shoppingCartRepository.findByUserEmail(email).orElseGet(() -> {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new EntityNotFoundException("Can't find user with email " + email));
            return shoppingCartRepository.save(new ShoppingCart(user));
        });

    }

    private boolean checkItemUser(Long cartId, String email) {
        CartItem cartItem = cartItemService.getById(cartId);
        return cartItem.getShoppingCart().getUser().getEmail().equals(email);
    }
}
