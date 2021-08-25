package com.example.ikane;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class BookRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(max = 20)
    private String isbn;

    @NotEmpty
    private String author;
}
