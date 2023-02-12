package com.eriklievaart.snuggle.boot;

import java.io.File;
import java.util.List;

import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.io.api.JvmPaths;
import com.eriklievaart.toolkit.lang.api.collection.ListTool;
import com.eriklievaart.toolkit.lang.api.str.StringBuilderWrapper;

public class Remember {

	private static File root = new File(JvmPaths.getJarDirOrRunDir(Remember.class), "data");
	private static File last = new File(root, "last.txt");

	public static void storeLast(List<File> files) {
		StringBuilderWrapper builder = new StringBuilderWrapper();
		for (File file : files) {
			builder.appendLine(file.getAbsolutePath());
		}
		FileTool.writeStringToFile(builder.toString(), last);
	}

	public static List<File> getLast() {
		List<String> paths = FileTool.readLines(last);
		return ListTool.map(paths, p -> new File(p));
	}
}
