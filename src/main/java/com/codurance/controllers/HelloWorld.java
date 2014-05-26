package com.codurance.controllers;

import static spark.Spark.*;

public class HelloWorld {

	public static void main(String[] args) {
		get("/hello", (request, response) -> {
			return "Hello, World!";
		});
	}

}
