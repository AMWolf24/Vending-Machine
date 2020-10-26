package com.techelevator;

public class Item {
	
	private String name;
	private double price;
	private String itemNumber;
	private int inventoryNum;
	private String category;
	private int numberSold;
	private double totalThisSale;
	
	public Item (String itemNumber, String name, double price, int inventoryNum, String category) {
		this.name = name;
		this.price = price;
		this.itemNumber = itemNumber;
		this.inventoryNum = inventoryNum;
		this.category = category;
		numberSold = 0;
		totalThisSale = 0;
	}

	/************
	 * Getters
	 ************/

	public void setInventoryNum(int inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public int getInventoryNum() {
		return inventoryNum;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getNumberSold() {
		return numberSold;
	}
	
	public void setNumberSold(int numberSold) {
		this.numberSold = numberSold;
	}

	public double getTotalThisSale() {
		return totalThisSale;
	}

	public void setTotalThisSale(double totalThisSale) {
		this.totalThisSale = totalThisSale;
	}

	/***********
	 * Methods
	 ***********/
	
	public String toString() {                            //Converts item description to a String
		if(inventoryNum == 0) {
			return itemNumber + " | " + name + " | " + price + " | Sold Out | " + category;
		}else
		
		return itemNumber + " | " + name + " | " + price + " | " + inventoryNum + " | " + category;
	}
	
	public String secretString() {
		if(inventoryNum == 0) {
			return itemNumber + " | " + name + " | " + price + " | Sold Out | " + category + " | " + numberSold;
		}else
		
		return itemNumber + " | " + name + " | " + price + " | " + inventoryNum + " | " + category + " | " + numberSold;
	}

}
