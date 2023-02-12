package com.eriklievaart.snuggle.web;

import java.io.IOException;

import com.eriklievaart.toolkit.io.api.StringBuilderOutputStream;

import uk.ac.ed.ph.snuggletex.InputError;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions.WebPageType;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptionsTemplates;

public class SnuggleController {

	public String snuggle() throws IOException {
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

		session.writeWebPage(options, new StringBuilderOutputStream());
		for (InputError error : session.getErrors()) {
			System.err.println(error);
		}
		try (StringBuilderOutputStream stream = new StringBuilderOutputStream()) {
			return stream.getResult();
		}
	}
}
