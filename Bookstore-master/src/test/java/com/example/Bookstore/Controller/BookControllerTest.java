package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.BookBody;
import com.example.Bookstore.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookBody testBookBody;
    private Page<BookBody> testPage;

    @BeforeEach
    void setUp() {
        testBookBody = new BookBody();
        testBookBody.setId(1L);
        testBookBody.setTitle("Test Book");
        testBookBody.setPrice(29.99);
        testBookBody.setCoverImageUrl("http://example.com/cover.jpg");
        testBookBody.setAuthors(Arrays.asList("Test Author"));

        List<BookBody> books = Arrays.asList(testBookBody);
        testPage = new PageImpl<>(books, PageRequest.of(0, 6), 1);
    }

    @Test
    void getAllBooks_ShouldReturnBooks_WhenNoParameters() {
        // Given
        when(bookService.getAllBooks(null, null, "title", 0)).thenReturn(testPage);

        // When
        ResponseEntity<Page<BookBody>> response = bookController.getAllBooks(null, null, "title", 0);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("Test Book");
        verify(bookService).getAllBooks(null, null, "title", 0);
    }

    @Test
    void getAllBooks_ShouldReturnBooks_WhenCategoryProvided() {
        // Given
        when(bookService.getAllBooks(1, null, "title", 0)).thenReturn(testPage);

        // When
        ResponseEntity<Page<BookBody>> response = bookController.getAllBooks(1, null, "title", 0);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(bookService).getAllBooks(1, null, "title", 0);
    }

    @Test
    void getAllBooks_ShouldReturnBooks_WhenSearchProvided() {
        // Given
        when(bookService.getAllBooks(null, "test", "title", 0)).thenReturn(testPage);

        // When
        ResponseEntity<Page<BookBody>> response = bookController.getAllBooks(null, "test", "title", 0);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(bookService).getAllBooks(null, "test", "title", 0);
    }

    @Test
    void getAllBooks_ShouldReturnBooks_WhenAllParametersProvided() {
        // Given
        when(bookService.getAllBooks(1, "test", "title", 0)).thenReturn(testPage);

        // When
        ResponseEntity<Page<BookBody>> response = bookController.getAllBooks(1, "test", "title", 0);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(bookService).getAllBooks(1, "test", "title", 0);
    }

    @Test
    void getAllBooks_ShouldUseDefaultValues_WhenOptionalParametersNotProvided() {
        // Given
        when(bookService.getAllBooks(null, null, "title", 0)).thenReturn(testPage);

        // When
        ResponseEntity<Page<BookBody>> response = bookController.getAllBooks(null, null, "title", 0);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookService).getAllBooks(null, null, "title", 0);
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {
        // Given
        when(bookService.getBookById(1)).thenReturn(testBookBody);

        // When
        ResponseEntity<BookBody> response = bookController.getBookById(1);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Book");
        assertThat(response.getBody().getId()).isEqualTo(1);
        verify(bookService).getBookById(1);
    }

    @Test
    void getBookById_ShouldPropagateException_WhenBookNotFound() {
        // Given
        when(bookService.getBookById(999)).thenThrow(new RuntimeException("Book not found"));

        // When & Then
        assertThatThrownBy(() -> bookController.getBookById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
        
        verify(bookService).getBookById(999);
    }
}
