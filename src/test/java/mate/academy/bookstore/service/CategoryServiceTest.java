package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
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
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Find all categories")
    public void findAll_AllCategories_Success() {
        Category firstCategory = getDetectiveCategory();
        Category secondCategory = getFantasyCategory();

        CategoryDto firstCategoryDto = new CategoryDto();
        CategoryDto secondCategoryDto = new CategoryDto();

        BeanUtils.copyProperties(firstCategory, firstCategoryDto);
        BeanUtils.copyProperties(secondCategory, secondCategoryDto);

        Pageable pageable = PageRequest.of(1, 2);
        Page<Category> page = new PageImpl<>(List.of(firstCategory, secondCategory));
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryDto);
        Mockito.when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(List.of(firstCategoryDto, secondCategoryDto), actual);
    }

    @Test
    @DisplayName("Get category by existed id")
    public void getById_ExistedId_Success() {
        Category category = getDetectiveCategory();
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Detective");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(1L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get category by non-existed id")
    public void getById_NonExistedId_ReturnException() {
        Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(2L));
    }

    @Test
    @DisplayName("Save category")
    public void save_ValidRequest_Success() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Detective");
        Category category = new Category();
        category.setName(requestDto.getName());
        Category savedCategory = new Category();
        savedCategory.setName(category.getName());
        savedCategory.setId(1L);

        CategoryDto expected = new CategoryDto();
        expected.setName(savedCategory.getName());
        expected.setId(savedCategory.getId());
        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(expected);

        CategoryDto actual = categoryService.save(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category with valid request")
    public void update_ValidRequest_Success() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Detective");
        Category category = new Category();
        category.setName("Detective");
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Detective");
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Detective");

        Mockito.when(categoryRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        CategoryDto actual = categoryService.update(1L, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category by non-existed id")
    public void update_NonExistedId_ReturnException() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Detective");

        Mockito.when(categoryRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(2L, requestDto));
    }

    private Category getDetectiveCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Detective");
        return category;
    }

    private Category getFantasyCategory() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Fantasy");
        return category;
    }
}
