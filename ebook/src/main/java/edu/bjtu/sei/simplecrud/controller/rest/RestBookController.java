package edu.bjtu.sei.simplecrud.controller.rest;

import edu.bjtu.sei.simplecrud.domain.Book;
import edu.bjtu.sei.simplecrud.exception.ResourceNotFoundException;
import edu.bjtu.sei.simplecrud.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Tag(name = "book", description = "Book API")

public class RestBookController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${msg.rows_per_page}")
    private int ROW_PER_PAGE;


    @Value("10")
    private int READ_PER_PAGE;

    @Autowired
    private BookService bookService;

    @Value("${msg.title}")
    private String title;

    
    @GetMapping(value = "/api/books")
    public List<Book> getBooks(
            @RequestParam(value = "page", defaultValue = "1") int pageNumber, HttpServletRequest request) {

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
        return books;
    }

    @Operation(summary = "Find A Book by ID", description = "Query a single book by its id", tags = { "book" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query success",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "4xx", description = "Book not found") })
    @GetMapping(value = "/api/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(book);
    }
    

    @Operation(summary = "Add a new book", description = "", tags = { "book" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "book created",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "4xx", description = "Invalid input"),
            @ApiResponse(responseCode = "5xx", description = "book already exists") })

    @PostMapping(value = "/api/books/add")
    public ResponseEntity<String> addBook(
            @Parameter(description="Book to add. Cannot null or empty.", required=true, schema=@Schema(implementation = Book.class))
            @RequestBody Book book) {
        try {
            bookService.save(book);
            return ResponseEntity.ok().body("Create a new book successfully");
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Create new book failed due to bad request contents.");
        }
    }


    @Operation(summary = "Update a Book", description = "", tags = { "book" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee updated",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "4xx", description = "Invalid input"),
            @ApiResponse(responseCode = "5xx", description = "Update Failed, because of bad request contents.") })

    @PutMapping(value = {"/api/books/{bookId}"})
    public ResponseEntity<String> updateBook(
            @Parameter(description="Book Id. Cannot be empty.", required=true)
            @PathVariable long bookId,
            @Parameter(description="Book to update. Cannot be null or empty.", required=true, schema=@Schema(implementation = Book.class))
            @RequestBody Book book) {
        try {
            book.setId(bookId);
            bookService.update(book);
            return ResponseEntity.ok().body("Update book successfully");
        } catch (Exception ex) {
            // log exception first, 
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Update book failed due to bad request contents:" + book.toString());
        }
    }

    @Operation(summary = "输入Book Id, 删除相应的Book记录", description = "Delete a book by its book id.", tags = { "book" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "5xx", description = "Delete operation failed due to Book not found") })

    @DeleteMapping(value = {"/api/books/{bookId}"})
    public ResponseEntity<String> deleteBookById(
            @Parameter(description="Book Id. Cannot be empty.", required=true)
            @PathVariable long bookId) {
        try {
            bookService.deleteById(bookId);
            return ResponseEntity.ok().body("Delete book successful: deleted book id = " +  bookId);
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Delete failed due to bad request book id :" + bookId);
        }
    }

    @GetMapping(value = {"/api/books/{bookId}/read"})
    public ResponseEntity<ReadPage> readDeleteBookById(
            Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber, @PathVariable long bookId) {
        Book book = null;
        try {
            book = bookService.findById(bookId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Book not found");
        }
        long count = book.count();

        ReadPage page = new ReadPage();
        page.setBook(book);
        page.setPageNumber(pageNumber);
        page.setTexts(book.getContent(pageNumber, READ_PER_PAGE));
//        model.addAttribute("texts", book.getContent(pageNumber, READ_PER_PAGE));
//        model.addAttribute("book", book);
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * READ_PER_PAGE) < count;
        page.setHasPrev(hasPrev);
        page.setPrev(pageNumber-1);
        page.setHasNext(hasNext);
        page.setNext(pageNumber+1);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return ResponseEntity.ok().body(page);
    }


    @RequestMapping(value = {"/api/books/{bookId}/download"})
    private String downloadFile(HttpServletResponse response, @PathVariable long bookId) {

        String downloadFilePath = "src/main/resources/WEB-INF/upload/" + bookId + ".txt";//被下载的文件在服务器中的路径,
        String fileName = bookId + ".txt";//被下载文件的名称

        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return "books/" + bookId;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败";
    }


    @RequestMapping(value = "/api/books/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file_upload") MultipartFile file, ModelMap modelMap, @PathVariable long bookId) {
        modelMap.addAttribute("image", file);
        String uploadDir = "src/main/resources/WEB-INF/upload/";
        // Save file on system
        if (!file.isEmpty()) {
            try{
                Path path = Paths.get(uploadDir + file.getOriginalFilename());
                Files.write(path, file.getBytes());
                //logger.info("write file to: {}", path);
            }catch (Exception e){
                logger.error("upload file error", e);
            }
        }

        return "book-list";
    }
}

@Getter
@Setter
@Validated
class ReadPage{
    String texts;
    Book book;
    Boolean hasPrev;
    Boolean hasNext;
    int pageNumber;
    int prev;
    int next;
}
