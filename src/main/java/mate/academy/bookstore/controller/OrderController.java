package mate.academy.bookstore.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto placeOrder(@Valid PlaceOrderDto placeOrderDto) {
        return orderService.placeOrder(placeOrderDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<OrderDto> getOrdersHistory(Authentication authentication) {
        return orderService.getAll(getUserEmail(authentication));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long id,
            Authentication authentication) {
        return  orderService.getOrderItemById(orderId, id, getUserEmail(authentication));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            UpdateOrderStatusDto updateStatusDto) {
        return orderService.updateStatus(id, updateStatusDto);
    }

    private String getUserEmail(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }
}
