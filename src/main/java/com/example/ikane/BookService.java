package com.example.ikane;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

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

    public Book findById(long id) {
        Optional<Book> requestedBook = this.bookRepository.findById(id);
        if(requestedBook.isEmpty()) {
            throw new BookNotFoundException(format("Book with id %s not found", id));
        }
        return requestedBook.get();
    }
}
