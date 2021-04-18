package edu.bjtu.sei.simplecrud.mapper;

import edu.bjtu.sei.simplecrud.domain.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
	
		void save(Book book);
		
		void delete(Long id);
		
		void update(Book book);
		
		Book find(Long id);
		
		List<Book> findAll();

		boolean existsById(Long id);
		
}
