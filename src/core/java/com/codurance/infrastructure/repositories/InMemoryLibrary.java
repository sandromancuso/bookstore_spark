package com.codurance.infrastructure.repositories;

import com.codurance.model.book.Book;
import com.codurance.model.book.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

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
	public int nextId() {
		return books.stream().mapToInt(b -> b.getId()).max().orElse(0) + 1;
	}

	@Override
	public List<Book> allBooks() {
		return unmodifiableList(books);
	}

	@Override
	public List<Book> booksWhichNameContains(String text) {
		return books.stream()
					.filter(book -> book.getName().toUpperCase().contains(text.toUpperCase()))
					.collect(toList());
	}
}
