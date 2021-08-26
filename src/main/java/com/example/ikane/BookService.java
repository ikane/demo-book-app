package com.example.ikane;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    final private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Long createNewBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setAuthor(bookRequest.getAuthor());
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());

        book = this.bookRepository.save(book);

        return book.getId();
    }

    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }
}
