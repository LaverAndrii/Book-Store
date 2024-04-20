package mate.academy.bookstore.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public void placeOrder() {

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public void getOrderHistory() {

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public void getOrderItems(@PathVariable Long orderId) {

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{id}")
    public void getOrderItemById(@PathVariable Long orderId, @PathVariable Long id) {

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public void updateOrderStatus(@PathVariable Long id) {

    }
}
