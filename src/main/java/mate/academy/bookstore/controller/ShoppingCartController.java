package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddCartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateItemsQuantityDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Add cart item", description = "Add cart item to shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ShoppingCartDto addToCart(
            @RequestBody AddCartItemDto addCartItemDto,
            Authentication authentication) {
        return shoppingCartService.addBook(addCartItemDto, getUserEmail(authentication));
    }

    @Operation(summary = "Get a shopping cart", description = "Get a shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartService.getShoppingCart(getUserEmail(authentication));
    }

    @Operation(summary = "Update cart item", description = "Update quantity of cart item")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateItemsQuantityDto updateQuantityDto) {
        return shoppingCartService.updateCartItem(
                cartItemId,
                updateQuantityDto,
                getUserEmail(authentication));
    }

    @Operation(summary = "Remove cart item", description = "Remove cart item from shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto removeFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        return shoppingCartService.removeBook(cartItemId, getUserEmail(authentication));
    }

    private String getUserEmail(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }
}
