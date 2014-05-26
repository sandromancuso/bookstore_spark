package com.codurance.controllers;

import com.codurance.infrastructure.template.jade.ViewModel;
import com.codurance.model.books.Book;

import java.util.ArrayList;
import java.util.List;

import static com.codurance.infrastructure.template.jade.JadeRenderer.render;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class HelloWorld {

	public static void main(String[] args) {
		staticFileLocation("/public");

		get("/hello", (request, response) -> "Hello, World!");

		get("/books", (request, response) -> {
			List<Book> books = new ArrayList<>();
			books.add(new Book(1, "Book A", true, 10));
			books.add(new Book(2, "Book B", false, 20));
			books.add(new Book(3, "Book C", true, 30));

			ViewModel viewModel = new ViewModel();
			viewModel.put("pageName", "Books");
			viewModel.put("books", books);

			return render("index.jade", viewModel);
		});
	}

}
