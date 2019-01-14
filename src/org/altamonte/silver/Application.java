package org.altamonte.silver;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Application {
	public static DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy hh:mm:ss a");

	public static Scanner scanner = new Scanner(System.in);

	public static Date getDate(String dateType) {
		Date date = null;
		while (date == null) {
			System.out.print(dateType + " (month-day-year)> ");
			String input = Application.scanner.nextLine();
			try {
				date = new SimpleDateFormat("MM-dd-yyyy").parse(input);
			} catch (ParseException e) {
				date = null;
			}
		}

		System.out.println(dateType + ": " + Application.dateFormat.format(date));

		return date;
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
			case "setc":
				// TODO: updates json files in conn directory
				break;
			case "test":
				break;
			case "pull":
				app.pullData();
				break;
			default:
				System.out.println("command '" + menuOption + "' not recognized...");
				System.out.println("enter 'help' for assistance");
			}
			System.out.print("> ");
			menuOption = Application.scanner.nextLine();
		}
		Application.scanner.close();
	}

	private ConnectionVariables xcadConnVars;

	private ConnectionVariables cafeConnVars;

	public Application() throws IOException {
		this.setup();
	}

	private void printMenu() {
		System.out.println(" help - Print this menu");
		System.out.println(" setc - Set connection variables");
		System.out.println(" test - Test database connection");
		System.out.println(" pull - Pull tables from database");
		System.out.println(" exit - Exit the program");
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

	private void pullData() {
		Date startDate = Application.getDate("start");
		Date stopDate = Application.getDate("stop");

		if (startDate.after(stopDate)) {
			System.out.println("dates out of order, swapping dates...");
			Date temp = startDate;
			startDate = stopDate;
			stopDate = temp;
			System.out.println("start: " + Application.dateFormat.format(startDate));
			System.out.println("stop: " + Application.dateFormat.format(stopDate));
		}

		// TODO: continue with pullData method --->

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

		String xcadJson = new String(Files.readAllBytes((Paths.get("./conn/xcadVars.json"))));
		String cafeJson = new String(Files.readAllBytes((Paths.get("./conn/cafeVars.json"))));
		this.xcadConnVars = new Gson().fromJson(xcadJson, ConnectionVariables.class);
		this.cafeConnVars = new Gson().fromJson(cafeJson, ConnectionVariables.class);
	}

}
