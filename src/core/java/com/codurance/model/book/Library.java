package com.codurance.model.book;

import java.util.List;
import java.util.Optional;

public interface Library {

	void add(Book book);

	Optional<Book> bookById(Integer id);

	int nextId();

	List<Book> allBooks();

	List<Book> booksWhichNameContains(String title);
}
