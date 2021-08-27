package com.example.ikane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<BookRequest> captor;

    @Test
    void shouldCreateNewBookInDBWhenPostRequestReceived() throws Exception {
        BookRequest bookRequest = BookRequest.builder()
                .title("Java 11")
                .author("Duke")
                .isbn("1337")
                .build();

        when(this.bookService.createNewBook(captor.capture())).thenReturn(1L);

        this.mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/books/1"))
        ;

        assertThat(captor.getValue().getTitle()).isEqualTo("Java 11");
        assertThat(captor.getValue().getAuthor()).isEqualTo("Duke");
        assertThat(captor.getValue().getIsbn()).isEqualTo("1337");
    }

    @Test
    void shouldFindAllBooks() throws Exception {
        when(this.bookService.findAll()).thenReturn(List.of(
                createBook(1L, "Java 11", "Duke", "1233"),
                createBook(2L, "Java EE8", "Duke", "123444")));

        this.mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Java 11")))
                .andExpect(jsonPath("$[0].author", is("Duke")))
                .andExpect(jsonPath("$[0].isbn", is("1233")))
        ;
    }

    @Test
    void shouldFindBookById() throws Exception {
        when(this.bookService.findById(1L)).thenReturn(createBook(1L, "Java 11", "Duke", "1233"));

        this.mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Java 11")))
                .andExpect(jsonPath("$.author", is("Duke")))
                .andExpect(jsonPath("$.isbn", is("1233")))
        ;
    }

    @Test
    void shouldReturnNotFoundForUnknownBook() throws Exception {
        when(this.bookService.findById(1L)).thenThrow(new BookNotFoundException("Book not found"));

        this.mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound())
        ;
    }

    private Book createBook(long id, String title, String author, String isbn) {
        Book book = new Book();

        book.setId(id);
        book.setAuthor(author);
        book.setTitle(title);
        book.setIsbn(isbn);

        return book;
    }
}