package mate.academy.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BookControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-some-books-to-table.sql",
            "classpath:database/categories/add-some-categories-to-table.sql",
                    "classpath:database/books/add-categories-to-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-categories-from-books.sql",
                    "classpath:database/books/remove-all-books-from-table.sql",
            "classpath:database/categories/remove-all-categories-from-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_AllBooks_Success() throws Exception {
        List<BookDto> expected = getBookDtos();

        MvcResult mvcResult = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>() {});

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get book by existed id")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-some-books-to-table.sql",
            "classpath:database/categories/add-some-categories-to-table.sql",
            "classpath:database/books/add-categories-to-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-categories-from-books.sql",
            "classpath:database/books/remove-all-books-from-table.sql",
            "classpath:database/categories/remove-all-categories-from-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getById_ValidId_Success() throws Exception {
        BookDto expected = getBookDtos().get(0);

        MvcResult mvcResult = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk()).andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Create book with valid request")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/clear-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void create_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Kobzar");
        requestDto.setAuthor("Shevchenko");
        requestDto.setPrice(BigDecimal.valueOf(500));
        requestDto.setIsbn("12345");
        requestDto.setCategoryIds(Set.of());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(post("/books").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        BookDto expected = getOneBookDto();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update book with valid request dto and by existed id")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/add-one-book-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateById_ValidUpdateDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Kobzar");
        requestDto.setAuthor("Shevchenko");
        requestDto.setPrice(BigDecimal.valueOf(500));
        requestDto.setIsbn("12345");
        requestDto.setCategoryIds(Set.of());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        BookDto expected = getOneBookDto();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Search books with valid parameters")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-one-book-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchBooks_ValidParams_Success() throws Exception {
        BookDto expected = getOneBookDto();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("titles", "Kobzar");

        MvcResult mvcResult = mockMvc.perform(get("/books/search").params(params))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(List.of(expected), actual);
    }

    private List<BookDto> getBookDtos() {
        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("Witcher");
        secondBook.setAuthor("Sapkowski");
        secondBook.setIsbn("11111");
        secondBook.setPrice(BigDecimal.valueOf(350));
        secondBook.setCategoryIds(Set.of(1L));

        BookDto thirdBook = new BookDto();
        thirdBook.setId(3L);
        thirdBook.setTitle("Just after sunset");
        thirdBook.setAuthor("King");
        thirdBook.setIsbn("22222");
        thirdBook.setPrice(BigDecimal.valueOf(300));
        thirdBook.setCategoryIds(Set.of(1L, 2L));

        BookDto firstBook = getOneBookDto();

        return List.of(firstBook, secondBook, thirdBook);
    }

    private BookDto getOneBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Kobzar");
        bookDto.setAuthor("Shevchenko");
        bookDto.setIsbn("12345");
        bookDto.setPrice(BigDecimal.valueOf(500));
        bookDto.setCategoryIds(Set.of());
        return bookDto;
    }
}
