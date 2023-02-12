package com.eriklievaart.snuggle.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.ac.ed.ph.snuggletex.InputError;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions.WebPageType;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptionsTemplates;

public class Main {

	public static void main(String[] args) throws IOException {
		SnuggleEngine engine = new SnuggleEngine();
		SnuggleSession session = engine.createSession();

		SnuggleInput input = new SnuggleInput("$\frac{1}{2}$");
		session.parseInput(input);

		WebPageOutputOptions options = WebPageOutputOptionsTemplates.createWebPageOptions(WebPageType.MOZILLA);
		options.setTitle("My Web Page");
		options.setAddingTitleHeading(true);
		options.setIndenting(true);
		options.setAddingMathSourceAnnotations(true);
		options.setIncludingStyleElement(false);

		session.writeWebPage(options, new FileOutputStream(new File("/tmp/test.html")));
		for (InputError error : session.getErrors()) {
			System.err.println(error);
		}
	}
}