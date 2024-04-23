package mate.academy.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.PlaceOrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Transactional
    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto, String email) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email).orElseThrow();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(placeOrderDto.shippingAddress());
        order.setTotal(getTotal(shoppingCart.getCartItems()));

        orderRepository.save(order);
        order.setOrderItems(shoppingCart.getCartItems().stream()
                .map(c -> convertCartToOrderItem(c, order))
                .collect(Collectors.toSet())
        );

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable, String email) {
        return orderRepository.findAllByUserEmail(pageable, email).stream()
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
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id " + id)
        );
        order.setStatus(orderStatusDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal getTotal(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(o -> o.getBook().getPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItem convertCartToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        orderItemRepository.save(orderItem);
        return orderItem;
    }
}
