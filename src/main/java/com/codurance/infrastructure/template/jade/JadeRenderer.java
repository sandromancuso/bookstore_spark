package com.codurance.infrastructure.template.jade;

import de.neuland.jade4j.Jade4J;

import java.io.IOException;

public class JadeRenderer {

	public static String BASE_TEMPLATE_FOLDER = "./src/main/static/public/templates/";

	public static String render(String template, ViewModel viewModel) {
		try {
			return Jade4J.render("./src/main/static/public/templates/index.jade", viewModel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
