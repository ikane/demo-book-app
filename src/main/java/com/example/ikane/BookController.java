package com.example.ikane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Void> createNewBook(@Validated @RequestBody BookRequest bookRequest) {
        this.bookService.createNewBook(bookRequest);

        URI uriLocation = null;
        return ResponseEntity.created(uriLocation);
    }
}
