package mate.academy.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("FROM Book b JOIN FETCH b.categories c WHERE c.id = :id")
    List<Book> findAllByCategoryId(Long id);

    @Query("FROM Book b LEFT JOIN FETCH b.categories")
    Page<Book> findAll(Pageable pageable);

    @Query("FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findById(Long id);
}
