package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Test
    @DisplayName("Save book")
    public void save_ValidRequest_Success() {
        CreateBookRequestDto requestDto = getKobzarRequestDto();
        Book book = new Book();
        BeanUtils.copyProperties(requestDto, book);
        Book savedBook = new Book();
        BeanUtils.copyProperties(book, savedBook);
        savedBook.setId(1L);
        BookDto expectedDto = new BookDto();
        BeanUtils.copyProperties(savedBook, expectedDto);

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actualDto = bookService.save(requestDto);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Find all books")
    public void findAll_AllBooks_Success() {
        Book firstBook = getKobzarBook();
        Book secondBook = getWitcherBook();
        BookDto firstBookDto = new BookDto();
        BookDto secondBookDto = new BookDto();
        BeanUtils.copyProperties(firstBook, firstBookDto);
        BeanUtils.copyProperties(secondBook, secondBookDto);
        Pageable pageable = PageRequest.of(1, 2);
        Page<Book> page = new PageImpl<>(List.of(firstBook, secondBook));

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        Mockito.when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> actual = bookService.findAll(pageable);
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find book by existed id")
    public void findById_ExistedId_Success() {
        Book book = getKobzarBook();
        BookDto expected = new BookDto();
        BeanUtils.copyProperties(book, expected);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find book by non-existed id")
    public void findById_NonExistedId_ThrowsException() {
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
    }

    @Test
    @DisplayName("Update book by existed id and with valid request")
    public void updateById_ValidRequest_Success() {
        CreateBookRequestDto requestDto = getKobzarRequestDto();

        Book book = new Book();
        Book updatedBook = new Book();
        BeanUtils.copyProperties(requestDto, book);
        BeanUtils.copyProperties(book, updatedBook);
        updatedBook.setId(1L);

        BookDto expected = new BookDto();
        BeanUtils.copyProperties(updatedBook, expected);
        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        Mockito.when(bookMapper.toDto(updatedBook)).thenReturn(expected);

        BookDto actual = bookService.updateById(1L, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update book by non-existed id")
    public void updateById_NonExistedId_ReturnException() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.updateById(1L, requestDto));
    }

    @Test
    @DisplayName("Search books by valid parameters")
    public void search_ValidParams_Success() {
        Book firstBook = getKobzarBook();
        Book secondBook = getWitcherBook();
        BookDto firstBookDto = new BookDto();
        BookDto secondBookDto = new BookDto();
        BeanUtils.copyProperties(firstBook, firstBookDto);
        BeanUtils.copyProperties(secondBook, secondBookDto);

        String[] authors = new String[0];
        String[] titles = new String[]{"Kob"};

        BookSearchParametersDto parmsDto = new BookSearchParametersDto(authors, titles);
        Pageable pageable = PageRequest.of(1, 2);

        Page<Book> page = new PageImpl<>(List.of(firstBook, secondBook));

        Mockito.when(bookRepository.findAll(
                bookSpecificationBuilder.build(parmsDto), pageable))
                .thenReturn(page);
        Mockito.when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        Mockito.when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> actual = bookService.search(parmsDto, pageable);
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find books by category id")
    public void findByCategoryId_ExistedId_Success() {
        Book book = getKobzarBook();
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        BeanUtils.copyProperties(book, bookDto);
        Mockito.when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(book));
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        List<BookDtoWithoutCategoryIds> actual = bookService.findByCategoryId(1L);

        assertEquals(List.of(bookDto), actual);
    }

    private Book getKobzarBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Kobzar");
        book.setAuthor("Shevchenko");
        book.setPrice(BigDecimal.valueOf(300));
        book.setIsbn("11111");
        return book;
    }

    private Book getWitcherBook() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Witcher");
        book.setAuthor("Sapkowski");
        book.setPrice(BigDecimal.valueOf(350));
        book.setIsbn("22222");
        return book;
    }

    private CreateBookRequestDto getKobzarRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Kobzar");
        requestDto.setAuthor("Shevchenko");
        requestDto.setPrice(BigDecimal.valueOf(300));
        requestDto.setIsbn("11111");
        return requestDto;
    }
}
