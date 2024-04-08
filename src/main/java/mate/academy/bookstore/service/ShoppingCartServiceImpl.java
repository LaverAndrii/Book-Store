package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateItemsQuantityDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    //private final CartItemMapper cartItemMapper;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;

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
        //ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow();
        cartItem.setQuantity(updateQuantityDto.quantity());
        /*CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(i -> i.getId().equals(cartId))
                .findFirst()
                .orElseThrow();
        cartItem.setQuantity(addCartItemDto.getQuantity());*/
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartByEmail(email));
    }

    @Override
    public void removeBook(Long cartId, String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);

        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow();
        shoppingCart.getCartItems().remove(cartItem);
        cartItem.setDeleted(true);

    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        return shoppingCartRepository.findByUserEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by email " + email));
    }
}
