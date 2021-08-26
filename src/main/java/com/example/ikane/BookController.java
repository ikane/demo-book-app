package com.example.ikane;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Void> createNewBook(@Valid @RequestBody BookRequest bookRequest, UriComponentsBuilder uriComponentsBuilder) {
        Long id = this.bookService.createNewBook(bookRequest);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/books/{id}").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        return ResponseEntity.ok(this.bookService.findAll());
    }
}
