package edu.bjtu.sei.simplecrud.controller;

import edu.bjtu.sei.simplecrud.domain.Contact;
import edu.bjtu.sei.simplecrud.exception.ResourceNotFoundException;
import edu.bjtu.sei.simplecrud.service.ContactService;
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
    private ContactService bookService;

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

        List<Contact> contacts = bookService.findAll(pageNumber, ROW_PER_PAGE);

        long count = bookService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("contacts", contacts);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "contact-list";
    }

    @GetMapping(value = "/books/{contactId}")
    public String getBookById(Model model, @PathVariable long contactId) {
        Contact contact = null;
        try {
            contact = bookService.findById(contactId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("contact", contact);
        model.addAttribute("contactimg","../images/p"+ contactId + ".jpg");
        return "contact";
    }

    @GetMapping(value = {"/books/add"})
    public String showAddBook(Model model) {
        Contact contact = new Contact();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "contact-edit";
    }

    @PostMapping(value = "/books/add")
    public String addBook(Model model,
            @ModelAttribute("contact") Contact contact) {        
        try {
            //Contact newContact = bookService.save(contact);
        	bookService.save(contact);
        	int p = (int) Math.ceil(bookService.count()/ROW_PER_PAGE)+1;
            return "redirect:/books?page=" + String.valueOf(p);
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("contact", contact);
            model.addAttribute("add", true);
            return "contact-edit";
        }        
    }

    @GetMapping(value = {"/books/{contactId}/edit"})
    public String showEditBook(Model model, @PathVariable long contactId) {
        Contact contact = null;
        try {
            contact = bookService.findById(contactId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("contact", contact);
        return "contact-edit";
    }

    @PostMapping(value = {"/books/{contactId}/edit"})
    public String updateBook(Model model,
            @PathVariable long contactId,
            @ModelAttribute("contact") Contact contact) {        
        try {
            contact.setId(contactId);
            bookService.update(contact);
            return "redirect:/books/" + String.valueOf(contact.getId());
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

             model.addAttribute("add", false);
            return "contact-edit";
        }
    }

    @GetMapping(value = {"/books/{contactId}/delete"})
    public String showDeleteBookById(
            Model model, @PathVariable long contactId) {
        Contact contact = null;
        try {
            contact = bookService.findById(contactId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("contact", contact);
        return "contact";
    }

    @PostMapping(value = {"/books/{contactId}/delete"})
    public String deleteBookById(
            Model model, @PathVariable long contactId) {
        try {
            bookService.deleteById(contactId);
            return "redirect:/books";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "contact";
        }
    }
}
