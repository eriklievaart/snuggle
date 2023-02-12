package com.eriklievaart.snuggle.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {

	public static void main(String[] args) throws IOException {
		try (Socket client = new Socket("localhost", 9876)) {
			PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
			writer.println(JOptionPane.showInputDialog("message to send?"));

			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.println("server: " + reader.readLine());
		}
	}
}
