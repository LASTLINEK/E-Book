package edu.bjtu.sei.simplecrud.controller;

import edu.bjtu.sei.simplecrud.domain.Book;
import edu.bjtu.sei.simplecrud.exception.ResourceNotFoundException;
import edu.bjtu.sei.simplecrud.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${msg.rows_per_page}")
    private int ROW_PER_PAGE;

    @Autowired
    private BookService bookService;

    @Value("${msg.title}")
    private String title;

    
    @GetMapping(value = "/books")
    public String getBooks(Model model,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber, HttpServletRequest request) {
    	
        @SuppressWarnings("unchecked")
        
		List<String> logs = (List<String>) request.getSession().getAttribute("LOGS_SESSION");
        //check if notes is present in session or not
        if (logs == null) {
            logs = new ArrayList<>();
            // if notes object is not present in session, set notes in the request session
            request.getSession().setAttribute("LOGS_SESSION", logs);
        }
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        //String log = request.getUserPrincipal().getName() + "/" + timeStamp + "/" + "list books";
        String log = timeStamp + "/" + "list books";
        logs.add(log);
        request.getSession().setAttribute("LOGS_SESSION", logs);

        List<Book> books = bookService.findAll(pageNumber, ROW_PER_PAGE);

        long count = bookService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "book-list";
    }

    @GetMapping(value = "/books/{bookId}")
    public String getBookById(Model model, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("book", book);
        model.addAttribute("bookimg","../images/p"+ bookId + ".jpg");
        return "book";
    }

    @GetMapping(value = {"/books/add"})
    public String showAddBook(Model model) {
        Book book = new Book();
        model.addAttribute("add", true);
        model.addAttribute("book", book);

        return "book-edit";
    }

    @PostMapping(value = "/books/add")
    public String addBook(Model model,
            @ModelAttribute("book") Book book) {
        try {
            //Book newBook = bookService.save(book);
        	bookService.save(book);
        	int p = (int) Math.ceil(bookService.count()/ROW_PER_PAGE)+1;
            return "redirect:/books?page=" + String.valueOf(p);
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("book", book);
            model.addAttribute("add", true);
            return "book-edit";
        }        
    }

    @GetMapping(value = {"/books/{bookId}/edit"})
    public String showEditBook(Model model, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping(value = {"/books/{bookId}/edit"})
    public String updateBook(Model model,
            @PathVariable long bookId,
            @ModelAttribute("book") Book book) {
        try {
            book.setId(bookId);
            bookService.update(book);
            return "redirect:/books/" + String.valueOf(book.getId());
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

             model.addAttribute("add", false);
            return "book-edit";
        }
    }

    @GetMapping(value = {"/books/{bookId}/delete"})
    public String showDeleteBookById(
            Model model, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping(value = {"/books/{bookId}/delete"})
    public String deleteBookById(
            Model model, @PathVariable long bookId) {
        try {
            bookService.deleteById(bookId);
            return "redirect:/books";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "book";
        }
    }
}
