package com.techelevator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

	private List<Item> stock;
	private double currentMoney;
	private File log;
	private PrintWriter actionLog;
	private File total;
	private String name;
	private double amountOfChange;

	public VendingMachine() throws IOException {
		stock = new ArrayList<>();
		currentMoney = 0;
		log = new File("Log.txt");
		FileWriter aFileWriter = new FileWriter(log,true);
		BufferedWriter aBufferedWriter = new BufferedWriter(aFileWriter);
		actionLog = new PrintWriter(aBufferedWriter);
		total = new File("total.txt");
		name = "";
        amountOfChange = 0;
	}

	/***********************
	 * Getters and Setter
	 ***********************/

	public List<Item> getStock() {
		return stock;
	}

	public double getCurrentMoney() {

		return currentMoney;
	}

	public void setCurrentMoney(double currentMoney) {
		this.currentMoney = currentMoney;
	}

	public File getLog() {
		return log;
	}

	public PrintWriter getActionLog() {
		return actionLog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**********
	 * Methods
	 **********/

	public List<Item> stockMachine() throws FileNotFoundException { // This fills the initial stock of the machine.
		File vendingItems = new File("vendingmachine.csv");
		Scanner fileReader = new Scanner(vendingItems);
		while (fileReader.hasNextLine()) {
			String theLine = fileReader.nextLine();
			String itemArray[] = theLine.split("\\|");

			if (itemArray[3].equals("Chip")) { // Morphs Item to Chip
				Chip goods = new Chip(itemArray[0], itemArray[1], Double.parseDouble(itemArray[2]), 5, itemArray[3]);
				stock.add(goods);
			} else if (itemArray[3].equals("Candy")) { // Morphs Item to Candy
				Candy goods = new Candy(itemArray[0], itemArray[1], Double.parseDouble(itemArray[2]), 5, itemArray[3]);
				stock.add(goods);
			} else if (itemArray[3].equals("Drink")) { // Morphs Item to Drink
				Drink goods = new Drink(itemArray[0], itemArray[1], Double.parseDouble(itemArray[2]), 5, itemArray[3]);
				stock.add(goods);
			} else if (itemArray[3].equals("Gum")) { // Morphs Item to Gum
				Gum goods = new Gum(itemArray[0], itemArray[1], Double.parseDouble(itemArray[2]), 5, itemArray[3]);
				stock.add(goods);
			}

		}
		fileReader.close();
		return stock;
	}

	public double feedMoney(double moneyAdded) { // adds money to the current money in the machine
		if (moneyAdded == 1.00 || moneyAdded == 2.00 || moneyAdded == 5.00 || moneyAdded == 10.00) {
			name = "FEED MONEY";
			amountOfChange = moneyAdded;
			currentMoney += moneyAdded;
			
		}
		return currentMoney;
	}

	public void returnMoney() { // re-dispenses money in machine in quarters dimes and nickels
		int quarters = 0;
		int dimes = 0;
		int nickels = 0;
		amountOfChange = currentMoney;
		name = "GIVE CHANGE";
		while (currentMoney > 0) {
			String doubleFormat = String.format("%.2f", currentMoney);
			currentMoney = Double.parseDouble(doubleFormat);
			if (currentMoney >= .25) {
				currentMoney -= .25;
				quarters++;
			} else if (currentMoney >= .1) {
				currentMoney -= .1;
				dimes++;
			} else if (currentMoney >= .05) {
				currentMoney -= .05;
				nickels++;
			}

		}
		System.out.println( // return statement
				"Your change is: " + quarters + " Quarter(s): " + dimes + " Dime(s): " + nickels + " Nickel(s):");

		
	}

	public void writeToSalesFile() throws IOException {
		

		LocalDateTime timeNow = LocalDateTime.now(); // to get the time in the file name
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM_dd_yyyy_hh_mm_a");
		String formattedDateTime = timeNow.format(formatter);

		File totalFile = new File("total.txt"); // to get the total
		Scanner totalReader = new Scanner(totalFile);
		double totalAmount = Double.parseDouble(totalReader.nextLine());

		File salesReader = new File("SalesReport.txt");
		PrintWriter salesReaderWriter = new PrintWriter(salesReader);
		salesReaderWriter.println(" ");
		for (Item i : getStock()) {
			salesReaderWriter.println(i.getName() + " | " + i.getNumberSold());
		}
		salesReaderWriter.println(" ");
		salesReaderWriter.printf("The total sales is: %.2f\n", totalAmount);
		salesReaderWriter.close();

		File sales = new File("SalesReport" + formattedDateTime + ".txt");
		if (!sales.isFile()) {
			PrintWriter salesLog = new PrintWriter(sales);
			for (Item i : getStock()) {
				salesLog.println(i.getName() + " | " + i.getNumberSold());
			}
			salesLog.println(" ");
			salesLog.printf("The total sales is: %.2f\n", totalAmount); // total amount
			salesLog.close();
		}

		// salesLog.println("Please work");
		// salesLog.close();
	}

	public void totalSales() throws FileNotFoundException {
		Scanner totalReader = new Scanner(total);

		double totalAmount = 0;
		totalAmount = Double.parseDouble(totalReader.nextLine());
		for (Item i : getStock()) {
			totalAmount = totalAmount + i.getTotalThisSale();
		}
		PrintWriter totalTracker = new PrintWriter(total);
		totalTracker.println(totalAmount);
		totalTracker.close();
	}

	public void writeToActionLog() throws IOException {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a");
		String formattedDateTime = now.format(formatter);

		

		actionLog.printf(formattedDateTime + " " + name + ": $%.2f ", amountOfChange );
		actionLog.printf("$%.2f\n", currentMoney);
		amountOfChange = 0;
		name = "";
		}
	

	public void selectProduct(String itemCode) { // product selector allows you to choose specific products
		name = "";
		amountOfChange = currentMoney;

		for (Item i : stock) {
			if (i.getItemNumber().equals(itemCode)) {
				name = i.getName();
				if (i.getInventoryNum() < 1) {
					name = "Failed Purchase";
					System.out.println("Sold Out");
					System.out.println("");
				} else

				if (currentMoney - i.getPrice() >= 0) {
					i.setInventoryNum(i.getInventoryNum() - 1);
					i.setNumberSold(i.getNumberSold() + 1);
					i.setTotalThisSale(i.getTotalThisSale() + i.getPrice());
					currentMoney -= i.getPrice();
					System.out.println("");
					System.out.print("Dispensing " + name + ": $" + i.getPrice() + " | ");
					System.out.printf("your remaining balance: $%.2f\n", currentMoney);

					if (i.getCategory().equals("Chip")) { // Chip dispensing message
						System.out.println("Crunch Crunch, Yum!");
						System.out.println("");
					} else if (i.getCategory().equals("Candy")) { // Candy dispensing message
						System.out.println("Munch Munch, Yum!");
						System.out.println("");
					} else if (i.getCategory().equals("Drink")) { // Drink dispensing message
						System.out.println("Glug Glug, Yum!");
						System.out.println("");
					} else if (i.getCategory().equals("Gum")) { // Gum dispensing message
						System.out.println("Chew Chew, Yum!");
						System.out.println("");
					}

				} else if(currentMoney -i.getPrice()<0) {
					name = "Failed Purchase";
					System.out.println("Insufficient funds.");
				System.out.println("");
				}
			}
		}
		if (name.equals("")) {
			name = "Invalid Option";
			System.out.println("Invalid Option.");
		}

	}

}
