package com.example.Bookstore.RequestBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class AddReviewBody {

    @NotNull
    @Range(min=1,max=5)
    private Integer rating;

    @NotBlank
    @Size(min=3,max=255)
    private String comment;

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
