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
import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
class BookControllerTest {

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
    @DisplayName("Create a new book")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/add-category-only.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("New Test Book")
                .setAuthor("New Test Author")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(25.50))
                .setDescription("Test Description")
                .setCoverImage("cover.jpg")
                .setCategoriesIds(Set.of(1L));

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoriesIds(requestDto.getCategoriesIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        boolean areEqual = EqualsBuilder.reflectionEquals(expected, actual, "id");
        assertTrue(areEqual, "The returned book DTO should match the expected DTO");
    }

    @Test
    @DisplayName("Find all books (when one book exists)")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsPageOfBooks_Success() throws Exception {
        // Given
        BookDto expected = getTestBookDto();

        // When
        MvcResult result = mockMvc.perform(get("/books?page=0&size=10"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        PageWrapper<BookDto> page = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        BookDto actual = page.getContent().get(0);
        boolean areEqual = EqualsBuilder.reflectionEquals(expected, actual);
        assertTrue(areEqual, "The book from the list should match the expected book");
    }

    @Test
    @DisplayName("Find book by ID (when book exists)")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_WithValidId_ReturnsBookDto() throws Exception {
        // Given
        Long bookId = 1L;
        BookDto expected = getTestBookDto();

        // When
        MvcResult result = mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        boolean areEqual = EqualsBuilder.reflectionEquals(expected, actual);
        assertTrue(areEqual, "The found book should match the expected book");
    }

    @Test
    @DisplayName("Update book (when book exists)")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_WithValidData_UpdatesAndReturnsBookDto() throws Exception {
        // Given
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setIsbn("9780321765723")
                .setPrice(BigDecimal.valueOf(99.99))
                .setDescription("Updated description")
                .setCoverImage("updated_cover.jpg")
                .setCategoriesIds(Set.of(1L));

        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoriesIds(requestDto.getCategoriesIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(put("/books/" + bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);

        boolean areEqual = EqualsBuilder.reflectionEquals(expected, actual);
        assertTrue(areEqual, "The updated book should match the request data");
    }

    @Test
    @DisplayName("Delete a book (when book exists)")
    @WithMockUser(roles = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:database/books/add-books-and-categories.sql")
    @Sql(scripts = "classpath:database/books/remove-all-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ById_DeletesBookAndReturnsNoContent() throws Exception {
        // Given
        Long bookId = 1L;

        // When
        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    private BookDto getTestBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("The Hobbit")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("978-0345339683")
                .setDescription("A fantasy novel")
                .setCoverImage("hobbit.jpg")
                .setPrice(new BigDecimal("15.99"))
                .setCategoriesIds(new HashSet<>(Set.of(1L)));
    }
}
