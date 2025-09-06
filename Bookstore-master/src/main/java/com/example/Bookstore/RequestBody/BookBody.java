package com.example.Bookstore.RequestBody;

import com.example.Bookstore.Model.Category;
import jakarta.validation.constraints.*;

import java.util.List;

public class BookBody {

    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    @NotEmpty(message = "At least one author is required")
    private List<@NotBlank(message = "Author name cannot be blank") String> authors;
    @Positive(message = "Price must be greater than 0")
    private double price;
    @NotBlank(message = "Cover image URL is required")
    @Size(max = 255, message = "Cover image URL must be less than 500 characters")
    private String coverImageUrl;
    @NotNull(message = "Category is required")
    private Category category;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getAuthors() {
        return authors;
    }
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getCoverImageUrl() {
        return coverImageUrl;
    }
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
