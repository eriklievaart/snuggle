package com.eriklievaart.snuggle.parse;

import java.util.Map;

import org.junit.Test;

import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.MapTool;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.lang.api.str.StringBuilderWrapper;

public class PreProcessorU {

	@Test
	public void substitute() {
		Map<?, ?> map = MapTool.of("23", "abcd");

		StringBuilderWrapper builder = new StringBuilderWrapper("0123456");
		PreProcessor.substitute(builder, 2, map);
		Check.isEqual(builder.toString(), "01abcd456");
	}

	@Test
	public void processMathModeEmpty() {
		StringBuilderWrapper builder = new StringBuilderWrapper();
		builder.appendLine("");

		String result = new PreProcessor().process(builder.toString());
		Check.isEqual(result, "\n");
	}

	@Test
	public void processMathModeSkip() {
		StringBuilderWrapper builder = new StringBuilderWrapper();
		builder.appendLine("$$ a=4 $$");

		String result = new PreProcessor().process(builder.toString());
		Check.isEqual(result, "$$ a=4 $$\n");
	}

	@Test
	public void processMathModeSimple() {
		StringBuilderWrapper builder = new StringBuilderWrapper();
		builder.appendLine("$$");
		builder.appendLine("a=4");
		builder.appendLine("$$");

		PreProcessor.processMathMode(builder);
		Check.isEqual(builder.toString(), "$$a=4$$\\\\\n");
	}

	@Test
	public void processMathModeLines() {
		StringBuilderWrapper builder = new StringBuilderWrapper();
		builder.appendLine("$$");
		builder.appendLine("a = b^2");
		builder.appendLine("b = \\sqrt{e^3 - 2}");
		builder.appendLine("$$");

		String result = new PreProcessor().process(builder.toString());

		String[] lines = Str.splitLines(result);
		Check.isEqual(lines[0], "$$a = b^2$$\\\\");
		Check.isEqual(lines[1], "$$b = \\sqrt{e^3 - 2}$$\\\\");
		CheckCollection.isSize(lines, 2);
	}
}
