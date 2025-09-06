package com.example.Bookstore.RequestBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeleteBookBody {

    @NotBlank
    @Size(min=3,max=255)
    private String title;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
