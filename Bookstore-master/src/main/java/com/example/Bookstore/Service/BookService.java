package com.example.Bookstore.Service;

import com.example.Bookstore.Model.Author;
import com.example.Bookstore.Model.Book;
import com.example.Bookstore.Model.Category;
import com.example.Bookstore.Repository.AuthorRepository;
import com.example.Bookstore.Repository.BookRepository;
import com.example.Bookstore.RequestBody.AddBookBody;
import com.example.Bookstore.RequestBody.BookBody;
import com.example.Bookstore.RequestBody.DeleteBookBody;
import com.example.Bookstore.RequestBody.UpdatePriceBookBody;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

//    public Page<Book> getAllBooks(Integer category, String search, String sortBy, int page) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(sortBy).ascending()); // 5 per page, sorted by sortBy
//
//        if (category != null && search != null) {
//            return bookRepository.findByCategoryIdAndTitleContainingIgnoreCase(category, search, pageable);
//        } else if (category != null) {
//            return bookRepository.findByCategoryId(category, pageable);
//        } else if (search != null) {
//            return bookRepository.findByTitleContainingIgnoreCase(search, pageable);
//        } else {
//            return bookRepository.findAll(pageable);
//        }
//    }
public Page<BookBody> getAllBooks(Integer categoryId, String search, String sortBy, int page) {
    Pageable pageable = PageRequest.of(page, 6, Sort.by(sortBy).ascending());

    Page<Book> booksPage;
    if (categoryId != null && search != null) {
        booksPage = bookRepository.findByCategoryIdAndTitleContainingIgnoreCase(categoryId, search, pageable);
    } else if (categoryId != null) {
        booksPage = bookRepository.findByCategoryId(categoryId, pageable);
    } else if (search != null) {
        booksPage = bookRepository.findByTitleContainingIgnoreCase(search, pageable);
    } else {
        booksPage = bookRepository.findAll(pageable);
    }

    return booksPage.map(book -> {
        BookBody dto = new BookBody();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
        dto.setPrice(book.getPrice());
        dto.setCoverImageUrl(book.getCoverImageUrl());

        if (book.getCategory() != null) {
            Category category = new Category();
            category.setId(book.getCategory().getId());
            category.setName(book.getCategory().getName());
            dto.setCategory(category);
        }

        return dto;
    });
}
    public BookBody getBookById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        BookBody dto = new BookBody();
        dto.setTitle(book.getTitle());
        dto.setAuthors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
        dto.setPrice(book.getPrice());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setCategory(book.getCategory());
        return dto;
    }


    // ADMIN
    public String addBook(AddBookBody addBookBody) {
        Optional<Book> existingTitle = bookRepository.findByTitle(addBookBody.getTitle());

        if (existingTitle.isPresent()) {
            throw new IllegalArgumentException("The book is already posted.");
        }

        Book book = new Book();
        book.setCoverImageUrl(addBookBody.getCover_image_url());
        book.setPrice(addBookBody.getPrice());
        book.setTitle(addBookBody.getTitle());


        Category category = new Category();
        category.setId(addBookBody.getCategory_id());
        book.setCategory(category);


        List<Author> authors = new ArrayList<>();
        if (addBookBody.getAuthors() != null) {
            for (String authorName : addBookBody.getAuthors()) {
                Author author = authorRepository.findByName(authorName)
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setName(authorName);
                            return authorRepository.save(newAuthor);
                        });
                authors.add(author);
            }
        }
        book.setAuthors(authors);

        bookRepository.save(book);
        return "Book added to the library.";
    }

    @Transactional
    public String deleteBook(DeleteBookBody deleteBookBody){
        Optional<Book> existingTitle = bookRepository.findByTitle(deleteBookBody.getTitle());
        if (existingTitle.isEmpty()) {
            throw new IllegalArgumentException("There is no book to be deleted.");
        }
        bookRepository.deleteBookByTitle(deleteBookBody.getTitle());
        return "Book deleted.";
    }

    public String updateBookPrice(UpdatePriceBookBody updatePriceBookBody){
        Book booktitle = bookRepository.findByTitle(updatePriceBookBody.getTitle()).get();

        if (booktitle!=null) {
            booktitle.setPrice(updatePriceBookBody.getPrice());
            bookRepository.save(booktitle);
        }
        else{
            throw new IllegalArgumentException("There is no book to be updated.");
        }
        return "Book price updated.";
    }
}
