package com.eriklievaart.snuggle.boot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.eriklievaart.snuggle.parse.PostProcessor;
import com.eriklievaart.snuggle.parse.PreProcessor;
import com.eriklievaart.snuggle.parse.SnuggleWrapper;
import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.ThrowableTool;
import com.eriklievaart.toolkit.lang.api.collection.FromCollection;
import com.eriklievaart.toolkit.lang.api.collection.ListTool;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Main {
	private static final LogTemplate log = new LogTemplate(Main.class);

	private static File destination = new File("/tmp/snuggle");

	public static void main(String[] args) throws Exception {
		if (destination.exists()) {
			destination.delete();
		}
		long start = System.currentTimeMillis();
		run(args);
		long spent = System.currentTimeMillis() - start;
		log.info("time spent rendering: " + spent + "ms");
	}

	private static void run(String[] args) throws IOException {
		List<String> argList = FromCollection.toList(args);
		if (args.length > 0 && argList.get(0).equals("-v")) {
			System.setProperty("verbose", "true");
			argList.remove(0);
		}
		try {
			render(argList.size() == 0 ? Remember.getLast() : convertToFiles(argList));
		} catch (Exception e) {
			FileTool.writeStringToFile("<pre>" + ThrowableTool.toString(e) + "</pre>", destination);
		}
		push(destination);
	}

	private static void render(List<File> files) throws IOException {
		SnuggleWrapper snuggle = new SnuggleWrapper();

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			for (File file : files) {
				log.info("rendering file: $", file);
				out.writeBytes(Str.sub("<h1>$</h1>\n", file.getName()).getBytes());
				String processed = new PreProcessor().process(FileTool.toString(file));
				snuggle.render(processed, out);
			}
			FileTool.writeStringToFile(new PostProcessor().process(out.toString()), destination);
		}
		Remember.storeLast(files);
	}

	private static void push(File file) throws IOException {
		String url = "http://localhost:8000/web/push";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);

		StreamTool.writeString("file=" + file.getPath(), con.getOutputStream());

		int responseCode = con.getResponseCode();
		log.info("POST URL: " + url + " status " + responseCode);
		log.info("response: " + StreamTool.toString(con.getInputStream()));
	}

	private static List<File> convertToFiles(List<String> paths) {
		return ListTool.map(paths, path -> new File(path));
	}
}
