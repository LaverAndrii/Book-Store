package mate.academy.bookstore.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        return null;
    }

    @Override
    public List<OrderDto> getAll(String email) {
        return orderRepository.findAllByUserEmail(email).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long id, String email) {
        Optional<OrderItem> orderItem = orderItemRepository.findByIdAndOrderId(id, orderId);
        if (orderItem.isEmpty() || !orderItem.get().getOrder().getUser().getEmail().equals(email)) {
            throw new EntityNotFoundException("Can't find order item by id " + id);
        }
        return orderItemMapper.toDto(orderItem.get());
    }

    @Transactional
    @Override
    public OrderDto updateStatus(Long id, UpdateOrderStatusDto orderStatusDto) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(orderStatusDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }
}
