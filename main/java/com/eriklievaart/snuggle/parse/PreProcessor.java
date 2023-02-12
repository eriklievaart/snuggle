package com.eriklievaart.snuggle.parse;

import java.util.List;
import java.util.Map;

import com.eriklievaart.toolkit.io.api.ResourceTool;
import com.eriklievaart.toolkit.io.api.SystemProperties;
import com.eriklievaart.toolkit.lang.api.collection.ListTool;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.lang.api.str.StringBuilderWrapper;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class PreProcessor {

	private LogTemplate log = new LogTemplate(getClass());

	public String process(String input) {
		StringBuilderWrapper builder = new StringBuilderWrapper(input);
		Map<String, String> replace = parseReplaceMap();

		if (SystemProperties.getBoolean("verbose")) {
			log.debug("PreProcessor input: <<<" + input + ">>>");
		}
		processMathMode(builder);
		for (int i = 0; i < builder.length(); i++) {
			substitute(builder, i, replace);
		}
		if (SystemProperties.getBoolean("verbose")) {
			log.debug("PreProcessor output: <<<" + builder + ">>>");
		}
		return builder.toString();
	}

	static void processMathMode(StringBuilderWrapper builder) {
		for (int o = 0; o < builder.length(); o++) {

			if (builder.substringAt(o, "$$")) {
				o = processMathChunk(builder, o + 2);
			}
		}
	}

	private static int processMathChunk(StringBuilderWrapper builder, int start) {
		boolean skip = true;

		for (int i = start; i < builder.length(); i++) {

			if (builder.charAt(i) == '\n') {
				skip = false;

			} else if (builder.substringAt(i, "$$")) {
				if (skip) {
					return i + 2;
				}
				int lastDollar = processMathChunk(builder, start, i) + 2;
				builder.insert(lastDollar, "\\\\");
				return lastDollar + 2;
			}
		}
		return builder.length();
	}

	private static int processMathChunk(StringBuilderWrapper builder, int start, int end) {
		String chunk = builder.deleteAt(start, end - start);
		List<String> lines = ListTool.filter(Str.splitLines(chunk), Str::notBlank);

		String replacement = Str.join(lines, "$$\\\\\n$$");
		builder.insert(start, replacement);
		return start + replacement.length();
	}

	private Map<String, String> parseReplaceMap() {
		Map<String, String> replace = NewCollection.map();
		for (String line : ResourceTool.getLines("/replace.txt")) {
			String[] keyValue = line.split("=", 2);
			replace.put(keyValue[0], keyValue[1]);
		}
		return replace;
	}

	static void substitute(StringBuilderWrapper builder, int index, Map<?, ?> replace) {
		replace.forEach((k, v) -> {
			if (builder.substringAt(index, k.toString())) {
				builder.deleteAt(index, k.toString().length());
				builder.insert(index, v.toString());
			}
		});
	}
}
