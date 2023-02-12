package com.eriklievaart.snuggle.parse;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import uk.ac.ed.ph.snuggletex.InputError;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.SnuggleSession.EndOutputAction;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptions.WebPageType;
import uk.ac.ed.ph.snuggletex.WebPageOutputOptionsTemplates;

public class SnuggleWrapper {
	private SnuggleEngine engine = new SnuggleEngine();

	public void render(String processed, ByteArrayOutputStream out) throws IOException {
		WebPageOutputOptions options = WebPageOutputOptionsTemplates.createWebPageOptions(WebPageType.MOZILLA);
		options.setAddingTitleHeading(true);
		options.setIndenting(true);
		options.setAddingMathSourceAnnotations(true);
		options.setIncludingStyleElement(false);

		SnuggleSession session = engine.createSession();
		session.parseInput(new SnuggleInput(processed));

		session.writeWebPage(options, new BufferedOutputStream(out), EndOutputAction.DO_NOTHING);
		for (InputError error : session.getErrors()) {
			System.err.println(error.getErrorCode());
		}
	}
}
