package com.codurance.infrastructure.template.jade;

import de.neuland.jade4j.Jade4J;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JadeRenderer {

	public static Path BASE_TEMPLATE_FOLDER = Paths.get("./src/main/static/public/templates/");

	public static String render(String template, ViewModel viewModel) {
		try {
			return Jade4J.render(BASE_TEMPLATE_FOLDER.resolve(template).toUri().toURL(), viewModel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
