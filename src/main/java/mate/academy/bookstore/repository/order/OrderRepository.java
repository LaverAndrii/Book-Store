package mate.academy.bookstore.repository.order;

import mate.academy.bookstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user.email = :email")
    Page<Order> findAllByUserEmail(Pageable pageable, String email);
}
