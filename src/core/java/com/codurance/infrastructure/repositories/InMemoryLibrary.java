package com.codurance.infrastructure.repositories;

import com.codurance.model.book.Book;
import com.codurance.model.book.Library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryLibrary implements Library {

	private List<Book> books = new ArrayList<>();

	@Override
	public void add(Book book) {
		this.books.add(book);
	}

	@Override
	public Optional<Book> bookById(Integer id) {
		return books.stream().filter(b -> b.getId() == id).findFirst();
	}

	@Override
	public int nextBookId() {
		return books.stream().mapToInt(b -> b.getId()).max().orElse(0) + 1;
	}

	@Override
	public List<Book> allBooks() {
		return Collections.unmodifiableList(books);
	}

	@Override
	public List<Book> booksWhichNameContains(String text) {
		return books.stream()
					.filter(book -> book.getName().toUpperCase().contains(text.toUpperCase()))
					.collect(Collectors.toList());
	}
}
