/**
 * Spring Boot + Thymeleaf Example 
 */
package edu.bjtu.sei.simplecrud.service;

import edu.bjtu.sei.simplecrud.domain.Book;
import edu.bjtu.sei.simplecrud.exception.BadResourceException;
import edu.bjtu.sei.simplecrud.exception.ResourceAlreadyExistsException;
import edu.bjtu.sei.simplecrud.exception.ResourceNotFoundException;
import edu.bjtu.sei.simplecrud.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class BookService {
    
    @Autowired
    //private BookRepository BookRepository;
    private BookMapper bookRepository;
    
    private boolean existsById(Long id) {
    	Book book = (Book) bookRepository.find(id);
        if (book==null) {
            return false;
        }
        else return true;    
    }
    
    public Book findById(Long id) throws ResourceNotFoundException {
        Book book = (Book) bookRepository.find(id);
        if (book==null) {
            throw new ResourceNotFoundException("Cannot find Book with id: " + id);
        }
        else return book;
    }
    
    public List<Book> findAll(int pageNumber, int rowPerPage) {
        List<Book> books = new ArrayList<>();
//        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, 
//                Sort.by("id").ascending());
        bookRepository.findAll().forEach(books::add);
		int size = books.size();
		int fromidx = (pageNumber-1)*rowPerPage;
		if (fromidx>size) fromidx = 0;
		int toidx = fromidx + rowPerPage;
		if (toidx > size) toidx = size;
		
		return books.subList(fromidx, toidx);

//        return contacts;
    }
    
    @SuppressWarnings("deprecation")
	public void save(Book book) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(book.getName())) {
            if (book.getId() != null && existsById(book.getId())) {
                throw new ResourceAlreadyExistsException("book with id: " + book.getId() +
                        " already exists");
            }
            bookRepository.save(book);
            //return book;
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save book");
            exc.addErrorMessage("book is null or empty");
            throw exc;
        }
    }
    
    @SuppressWarnings("deprecation")
	public void update(Book book)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(book.getName())) {
            if (!existsById(book.getId())) {
                throw new ResourceNotFoundException("Cannot find book with id: " + book.getId());
            }
            bookRepository.update(book);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save book");
            exc.addErrorMessage("book is null or empty");
            throw exc;
        }
    }
    
    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find book with id: " + id);
        }
        else {
            bookRepository.delete(id);
        }
    }
    
    public int count() {
    	
        return bookRepository.findAll().size();
    }
}
