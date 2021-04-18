package edu.bjtu.sei.simplecrud.domain;


import java.io.Serializable;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
public class Book implements Serializable {

    private static final long serialVersionUID = 4048798961366546485L;

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    //@Pattern(regexp ="^(0[0-9]{2,3}/-)?([2-9][0-9]{6,7})+(/-[0-9]{1,4})?$", message = "Phone number")
    @Size(max = 25)
    private String author;

    @Size(max = 100)
    private String path;

    @Size(max = 1024)
    private String content;

    @Size(max = 1024)
    private String note;

    public void setBook(Long id, String name, String author, String path, String content, String note){
        this.id = id;
        this.name = name;
        this.author = author;
        this.path = path;
        this.content = content;
        this.note = note;
    }

    public String getContent(){

        return "书的内容";
    }
}
