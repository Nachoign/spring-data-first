package com.nacho.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nacho.model.Book;

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
	
	List<Book> findByTittle(@Param("tittle") String tittle);
	List<Book> findByTittleLike(@Param("tittle") String tittle);
	List<Book> findByTittleContaining(@Param("tittle") String tittle);

}
