package com.loievroman.bookstoreapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loievroman.bookstoreapp.dto.category.CategoryDto;
import com.loievroman.bookstoreapp.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @lombok.Data
    static class PageWrapper<T> {
        private List<T> content;
        private long totalElements;
    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Create a new category")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("New Test Category")
                .setDescription("A description for the new category");
        CategoryDto expectedDto = new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actualDto.getId());
        boolean areEqual = EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
        assertTrue(areEqual, "The created category should match the request data.");
    }

    @Test
    @DisplayName("Find all categories (when one exists)")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_Paginated_ReturnsPageOfCategories() throws Exception {
        // Given
        CategoryDto expectedDto = getTestCategoryDto();

        // When
        MvcResult result = mockMvc.perform(get("/categories?page=0&size=10"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        PageWrapper<CategoryDto> page = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertEquals(1, page.getTotalElements());
        CategoryDto actualDto = page.getContent().get(0);
        boolean areEqual = EqualsBuilder.reflectionEquals(expectedDto, actualDto);
        assertTrue(areEqual, "The category from the list should match the expected one.");
    }

    @Test
    @DisplayName("Find category by ID (when one exists)")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_WithValidId_ReturnsCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryDto expectedDto = getTestCategoryDto();

        // When
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean areEqual = EqualsBuilder.reflectionEquals(expectedDto, actualDto);
        assertTrue(areEqual, "The found category should match the expected one.");
    }

    @Test
    @DisplayName("Update category (when one exists)")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_WithValidData_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryDto updateRequestDto = new CategoryDto()
                .setId(categoryId)
                .setName("Updated Category Name")
                .setDescription("Updated description");
        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        // When
        MvcResult result = mockMvc.perform(put("/categories/" + categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean areEqual = EqualsBuilder.reflectionEquals(updateRequestDto, actualDto);
        assertTrue(areEqual, "The updated category should match the request data.");
    }

    @Test
    @DisplayName("Delete a category (when one exists)")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ById_Success() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isNotFound());
    }

    private CategoryDto getTestCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("Fantasy")
                .setDescription("Fantasy category");
    }
}
