package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place order", description = "Place order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto placeOrder(
            @RequestBody @Valid PlaceOrderDto placeOrderDto,
            Authentication authentication) {
        return orderService.placeOrder(placeOrderDto, getUserEmail(authentication));
    }

    @Operation(summary = "Get orders", description = "Get all orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<OrderDto> getOrdersHistory(Pageable pageable, Authentication authentication) {
        return orderService.getAll(pageable, getUserEmail(authentication));
    }

    @Operation(summary = "Get order items", description = "Get all order items by order id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @Operation(summary = "Get order item", description = "Get order item by id and order id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long id,
            Authentication authentication) {
        return orderService.getOrderItemById(orderId, id, getUserEmail(authentication));
    }

    @Operation(summary = "Update status", description = "Update order status by order id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusDto updateStatusDto) {
        return orderService.updateStatus(id, updateStatusDto);
    }

    private String getUserEmail(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }
}
