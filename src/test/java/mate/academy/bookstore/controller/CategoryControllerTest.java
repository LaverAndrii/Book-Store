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
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.dto.category.CreateCategoryRequestDto;
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
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class CategoryControllerTest {
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
    @DisplayName("Get all categories")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-all-categories-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_AllCategories_Success() throws Exception {
        CategoryDto first = getFantasyCategoryDto();

        CategoryDto second = new CategoryDto();
        second.setId(2L);
        second.setName("Detective");

        List<CategoryDto> expected = List.of(first, second);

        MvcResult mvcResult = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Create category with valid request")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-all-categories-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_ValidRequest_Success() throws Exception {
        CategoryDto expected = getFantasyCategoryDto();

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Fantasy");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                post("/categories").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get category by existed id")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-all-categories-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ExistedId_Success() throws Exception {
        CategoryDto expected = getFantasyCategoryDto();

        MvcResult mvcResult = mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category by existed id and with valid request")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-all-categories-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidRequest_Success() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Biography");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Biography");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(
                put("/categories/1").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get books by existed category id")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-some-books-to-table.sql",
            "classpath:database/categories/add-some-categories-to-table.sql",
            "classpath:database/books/add-categories-to-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-categories-from-books.sql",
            "classpath:database/books/remove-all-books-from-table.sql",
            "classpath:database/categories/remove-all-categories-from-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_ExistedId_Success() throws Exception {
        BookDtoWithoutCategoryIds first = new BookDtoWithoutCategoryIds();
        first.setId(2L);
        first.setTitle("Witcher");
        first.setAuthor("Sapkowski");
        first.setIsbn("11111");
        first.setPrice(BigDecimal.valueOf(350));

        BookDtoWithoutCategoryIds second = new BookDtoWithoutCategoryIds();
        second.setId(3L);
        second.setTitle("Just after sunset");
        second.setAuthor("King");
        second.setIsbn("22222");
        second.setPrice(BigDecimal.valueOf(300));

        List<BookDtoWithoutCategoryIds> expected = List.of(first, second);

        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(expected, actual);
    }

    private CategoryDto getFantasyCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fantasy");
        return categoryDto;
    }
}
