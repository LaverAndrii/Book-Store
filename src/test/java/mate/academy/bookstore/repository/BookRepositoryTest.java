package mate.academy.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by existed category id")
    @Sql(scripts = {"classpath:database/books/add-some-books-to-table.sql",
            "classpath:database/categories/add-some-categories-to-table.sql",
            "classpath:database/books/add-categories-to-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-categories-from-books.sql",
            "classpath:database/books/remove-all-books-from-table.sql",
            "classpath:database/categories/remove-all-categories-from-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ExistedId_Success() {
        Category fantasy = new Category(1L);
        fantasy.setName("Fantasy");

        Book firstBook = new Book();
        firstBook.setId(2L);
        firstBook.setTitle("Witcher");
        firstBook.setAuthor("Sapkowski");
        firstBook.setIsbn("11111");
        firstBook.setPrice(BigDecimal.valueOf(350));
        firstBook.setCategories(Set.of(fantasy));

        Book secondBook = new Book();
        secondBook.setId(3L);
        secondBook.setTitle("Just after sunset");
        secondBook.setAuthor("King");
        secondBook.setIsbn("22222");
        secondBook.setPrice(BigDecimal.valueOf(300));
        secondBook.setCategories(Set.of(fantasy));

        List<Book> expected = List.of(firstBook, secondBook);

        List<Book> actual = bookRepository.findAllByCategoryId(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
