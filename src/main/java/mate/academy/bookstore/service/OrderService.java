package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderDto placeOrderDto, String email);

    List<OrderDto> getAll(Pageable pageable, String email);

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItemById(Long orderId, Long id, String email);

    OrderDto updateStatus(Long id, UpdateOrderStatusDto orderStatusDto);
}
