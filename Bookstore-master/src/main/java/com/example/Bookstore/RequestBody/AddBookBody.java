package com.example.Bookstore.RequestBody;

import jakarta.validation.constraints.*;

import java.util.List;

public class AddBookBody {

    @NotEmpty
    private List<String> authors;
    @NotBlank
    @Size( max = 255)
    private String cover_image_url;
    @NotNull
    @Positive
    private double price;
    @NotBlank
    private String title;
    @NotNull
    @Positive
    private Integer category_id;

    public List<String> getAuthors() {
        return authors;
    }
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
    public String getCover_image_url() {
        return cover_image_url;
    }
    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getCategory_id() {
        return category_id;
    }
    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }


}
