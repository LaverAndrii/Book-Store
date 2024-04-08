package mate.academy.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateItemsQuantityDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ShoppingCartDto addToCart(
            @RequestBody AddCartItemDto addCartItemDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return shoppingCartService.addBook(addCartItemDto, user.getEmail());
    }

    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getEmail());
    }

    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateItemsQuantityDto updateQuantityDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(cartItemId, updateQuantityDto, user.getEmail());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public void removeFromCart(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.removeBook(cartItemId, user.getEmail());
    }

}
