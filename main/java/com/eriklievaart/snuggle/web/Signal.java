package com.eriklievaart.snuggle.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Signal {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("expecting exactly 1 argument; the path to monitor");
			System.exit(44);
		}
		try (ServerSocket server = new ServerSocket(9876)) {
			while (true) {
				Socket client = server.accept();
				new Thread(() -> handle(client)).start();
			}
		}
	}

	private static void handle(Socket client) {
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String received = in.readLine();
			System.out.println("received: " + received);

			switch (received) {

			case "shutdown":
				out.println("shutting down");
				client.close();
				System.exit(0);
				return;

			case "hello":
				out.println("hello client");
				client.close();
				return;

			default:
				out.println("unknown command: " + received);
				client.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
