package com.example.Bookstore.Service;

import com.example.Bookstore.Model.Author;
import com.example.Bookstore.Model.Book;
import com.example.Bookstore.Model.Category;
import com.example.Bookstore.Repository.AuthorRepository;
import com.example.Bookstore.Repository.BookRepository;
import com.example.Bookstore.RequestBody.BookBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private Category testCategory;
    private Author testAuthor;
    private BookBody expectedBookBody;

    @BeforeEach
    void setUp() {

        testCategory = new Category();
        testCategory.setId(1);
        testCategory.setName("Fiction");

        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("Test Author");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setPrice(29.99);
        testBook.setCoverImageUrl("http://example.com/cover.jpg");
        testBook.setCategory(testCategory);
        testBook.setAuthors(Arrays.asList(testAuthor));


        expectedBookBody = new BookBody();
        expectedBookBody.setId(1L);
        expectedBookBody.setTitle("Test Book");
        expectedBookBody.setPrice(29.99);
        expectedBookBody.setCoverImageUrl("http://example.com/cover.jpg");
        expectedBookBody.setCategory(testCategory);
        expectedBookBody.setAuthors(Arrays.asList("Test Author"));
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks_WhenNoFilters() {
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(testBook));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<BookBody> result = bookService.getAllBooks(null, null, "title", 0);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllBooks_ShouldReturnFilteredBooks_WhenCategoryFilter() {
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(testBook));
        when(bookRepository.findByCategoryId(eq(1), any(Pageable.class))).thenReturn(bookPage);

        Page<BookBody> result = bookService.getAllBooks(1, null, "title", 0);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByCategoryId(eq(1), any(Pageable.class));
    }

    @Test
    void getAllBooks_ShouldReturnFilteredBooks_WhenSearchFilter() {

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(testBook));
        when(bookRepository.findByTitleContainingIgnoreCase(eq("test"), any(Pageable.class))).thenReturn(bookPage);

        Page<BookBody> result = bookService.getAllBooks(null, "test", "title", 0);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByTitleContainingIgnoreCase(eq("test"), any(Pageable.class));
    }

    @Test
    void getAllBooks_ShouldReturnFilteredBooks_WhenBothCategoryAndSearchFilters() {
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(testBook));
        when(bookRepository.findByCategoryIdAndTitleContainingIgnoreCase(eq(1), eq("test"), any(Pageable.class))).thenReturn(bookPage);

        Page<BookBody> result = bookService.getAllBooks(1, "test", "title", 0);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByCategoryIdAndTitleContainingIgnoreCase(eq(1), eq("test"), any(Pageable.class));
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {

        when(bookRepository.findById(1)).thenReturn(Optional.of(testBook));

        BookBody result = bookService.getBookById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Book");
        assertThat(result.getPrice()).isEqualTo(29.99);
        assertThat(result.getAuthors()).containsExactly("Test Author");
        verify(bookRepository).findById(1);
    }

    @Test
    void getBookById_ShouldThrowException_WhenBookNotFound() {

        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found");
    }

}
