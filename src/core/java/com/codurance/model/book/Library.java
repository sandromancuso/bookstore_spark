package com.codurance.model.book;

import java.util.List;

public interface Library {

	void add(Book book);

	List<Book> allBooks();

	List<Book> findBookNamed(String title);
}
