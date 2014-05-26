package com.codurance.controllers;

import com.codurance.model.books.Book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.neuland.jade4j.Jade4J.render;
import static spark.Spark.*;

public class HelloWorld {

	public static void main(String[] args) {
		staticFileLocation("/public");

		get("/hello", (request, response) -> {
			return "Hello, World!";
		});

		get("/books", (request, response) -> {
			List<Book> books = new ArrayList<>();
			books.add(new Book(1, "Book A", true, 10));
			books.add(new Book(2, "Book B", false, 20));
			books.add(new Book(3, "Book C", true, 30));

			Map<String, Object> model = new HashMap<>();
			model.put("pageName", "Books");
			model.put("books", books);
			String html = null;
			try {
				html = render("./src/main/static/public/templates/index.jade", model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return html;
		});
	}

}
