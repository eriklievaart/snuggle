package com.eriklievaart.snuggle.parse;

public class PostProcessor {

	public String process(String input) {
		return input.replaceAll("<math display=\"block\"", "<math display=\"inline\"");
	}
}
