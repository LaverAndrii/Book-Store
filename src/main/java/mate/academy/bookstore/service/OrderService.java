package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderDto> getAll(String email);

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItemById(Long orderId, Long id, String email);

    OrderDto updateStatus(Long id, UpdateOrderStatusDto orderStatusDto);
}
