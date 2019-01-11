package org.altamonte.silver;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Application {
	private ConnectionVariables conVars;
	private Scanner scanner;

	public Application() throws IOException {
		this.scanner = new Scanner(System.in);
		this.setup();
	}

	public static void main(String[] args) throws IOException {
		Application app = new Application();
		app.printWelcome();

		String menuOption = "help";

		while (!menuOption.equals("exit")) {
			switch (menuOption) {
			case "help":
				app.printMenu();
				break;
			default:
				System.out.println("command '" + menuOption + "' not recognized...");
				System.out.println("enter 'help' for assistance");
			}
			System.out.print("> ");
			menuOption = app.scanner.nextLine();
		}
		app.scanner.close();
	}

	private void printMenu() {
		System.out.println(" help - Print this menu");
		System.out.println(" exit - Exit the program");
	}

	private void setup() throws IOException {
		if (Files.notExists(Paths.get("./csv"))) {
			System.out.println("*** creating csv folder ***");
			Files.createDirectory(Paths.get("./csv"));
		}

		if (Files.notExists(Paths.get("./conn/xcadVars.json")) || Files.notExists(Paths.get("./conn/cafeVars.json"))) {
			System.out.println("*** creating new connection variables file ***");

			Files.createDirectory(Paths.get("./conn"));
			Files.createFile(Paths.get("./conn/xcadVars.json"));
			Files.createFile(Paths.get("./conn/cafeVars.json"));

			BufferedWriter xcadWriter = new BufferedWriter(new FileWriter("./conn/xcadVars.json"));
			BufferedWriter cafeWriter = new BufferedWriter(new FileWriter("./conn/cafeVars.json"));

			ConnectionVariables xcadVars = new ConnectionVariables("XCADHistory");
			ConnectionVariables cafeVars = new ConnectionVariables("CafeHist");

			xcadWriter.write(new Gson().toJson(xcadVars));
			cafeWriter.write(new Gson().toJson(cafeVars));

			xcadWriter.close();
			cafeWriter.close();
		}
	}

	private void printWelcome() {
		System.out.print("\u250c");
		for (int i = 0; i < 56; i++) {
			System.out.print("\u2500");
		}
		System.out.println("\u2510");
		System.out.println("\u2502  _____                                _                \u2502");
		System.out.println("\u2502 | ____|__  __ ___  __ _ __   __ __ _ | |_  ___   _ __  \u2502");
		System.out.println("\u2502 |  _|  \\ \\/ // __|/ _` |\\ \\ / // _` || __|/ _ \\ | '__| \u2502");
		System.out.println("\u2502 | |___  >  <| (__| (_| | \\ V /| (_| || |_| (_) || |    \u2502");
		System.out.println("\u2502 |_____|/_/\\_\\\\___|\\__,_|  \\_/  \\__,_| \\__|\\___/ |_|    \u2502");
		System.out.println("\u2502                            Version 0.0.0, January 2019 \u2502");
		System.out.print("\u2514");
		for (int i = 0; i < 56; i++) {
			System.out.print("\u2500");
		}
		System.out.println("\u2518");
	}

}
