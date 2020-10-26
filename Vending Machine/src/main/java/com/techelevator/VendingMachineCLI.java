package com.techelevator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*
*  It is the main process for the Vending Machine
*
*  THIS is where most, if not all, of your Vending Machine interactions should be coded
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code vending machine related code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu;         // Gain access to Menu class provided for the Capstone

public class VendingMachineCLI {

    // Main menu options defined as constants

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE      = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT          = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT  = "Sales Report";
	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS,
													    MAIN_MENU_OPTION_PURCHASE,
													    MAIN_MENU_OPTION_EXIT,
													    MAIN_MENU_OPTION_SALES_REPORT
													    };
	private static final String[] PURCHASE_MENU_OPTIONS  = {PURCHASE_MENU_FEED_MONEY,
															PURCHASE_MENU_SELECT_PRODUCT,
															PURCHASE_MENU_FINISH_TRANSACTION
															};
	
	
	private Menu vendingMenu;              // Menu object to be used by an instance of this class
	

	
	public VendingMachineCLI(Menu menu) {  // Constructor - user will pas a menu for this class to use
		this.vendingMenu = menu;           // Make the Menu the user object passed, our Menu

	}
	
	 
	/**************************************************************************************************************************
	*  VendingMachineCLI main processing loop
	*  
	*  Display the main menu and process option chosen
	*
	*  It is invoked from the VendingMachineApp program
	*
	*  THIS is where most, if not all, of your Vending Machine objects and interactions 
	*  should be coded
	*
	*  Methods should be defined following run() method and invoked from it
	 * @throws FileNotFoundException 
	*
	***************************************************************************************************************************/
	                //Instantiate Object
	
	public void run() throws IOException  {
		VendingMachine yourFood = new VendingMachine();
		
		yourFood.stockMachine();
		
		boolean shouldProcess = true;         // Loop control variable
		
		while(shouldProcess) {  
			// Loop until user indicates they want to exit
			
			System.out.println("");
			System.out.printf("Your current balance is: $%.2f\n" , yourFood.getCurrentMoney());
			String choice = (String)vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS);  // Display menu and get choice
			
			switch(choice) {                  // Process based on user menu choice
			
				case MAIN_MENU_OPTION_DISPLAY_ITEMS:
					displayItems(yourFood);   // invoke method to display items in Vending Machine
					;
					break;                    // Exit switch statement
			
				case MAIN_MENU_OPTION_PURCHASE:
					purchaseItems(yourFood);          // invoke method to purchase items from Vending Machine
					break;                    // Exit switch statement
			
				case MAIN_MENU_OPTION_EXIT:
					  // Invoke method to perform end of method processing
					endMethodProcessing(yourFood); 
					shouldProcess = false; // Set variable to end loop
					System.out.println("");
					System.out.println("Thank you and have a great day!");
					break;                    // Exit switch statement
				
				case MAIN_MENU_OPTION_SALES_REPORT:
					displaySalesReport(yourFood);
					break;
				
			}	
		}
		return;                               // End method and return to caller
	}
	
/********************************************************************************************************
 * Methods used to perform processing
 * @throws FileNotFoundException 
 ********************************************************************************************************/
	public void displayItems(VendingMachine displayMachine) {      // static attribute used as method is not associated with specific object instance
		// Code to display items in Vending Machine
		
		System.out.println("");
		for (Item i : displayMachine.getStock()) {
			System.out.println(i.toString());
		}
	}
	
	public void purchaseItems(VendingMachine yourFood) throws IOException {	 // static attribute used as method is not associated with specific object instance
		// Code to purchase items from Vending Machine
		boolean shouldProcess = true;         // Loop control variable

		while(shouldProcess) {                // Loop until user indicates they want to exit
			
			
			System.out.printf("Your current balance is: $%.2f\n" , yourFood.getCurrentMoney());
			String choice = (String)vendingMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);  // Display menu and get choice
			Scanner theKeyboard = new Scanner(System.in);
			switch(choice) {                  // Process based on user menu choice
			
				case PURCHASE_MENU_FEED_MONEY:
					System.out.println("How much money would you like to add? $1/$2/$5/$10");
					
					double moneyAdded = theKeyboard.nextDouble();
					
					yourFood.feedMoney(moneyAdded);// invoke method to feed money into machine
					yourFood.writeToActionLog();
					System.out.println(" ");
					break;                    // Exit switch statement
			
				case PURCHASE_MENU_SELECT_PRODUCT:
					
					displayItems(yourFood); // invoke method to select and purchase items from Vending Machine
					String itemCode = theKeyboard.nextLine();
					yourFood.selectProduct(itemCode);
					yourFood.writeToActionLog();
					yourFood.totalSales();
					break;                    // Exit switch statement
			
				case PURCHASE_MENU_FINISH_TRANSACTION:
					yourFood.returnMoney();   // returns your money in change from machine
					yourFood.writeToActionLog();
					yourFood.writeToSalesFile();
					
					shouldProcess = false;    // Set variable to end loop
					System.out.println("");
					System.out.println("Enjoy your food! Remember to take your change!");
					break;                    // Exit switch statement
					
				
			}	
		}
		
			
	}
	
	

	public void endMethodProcessing(VendingMachine displayLog) throws IOException { // static attribute used as method is not associated with specific object instance
		// Any processing that needs to be done before method ends
		displayLog.setName("EXIT");
		displayLog.writeToActionLog();
		displayLog.getActionLog().close();
	}
	
	public void displaySalesReport(VendingMachine displaySales) throws IOException {
		// whatever needs to be here
		
		displaySales.setName("SHOW SALES LOG");
		
		File sales = new File("SalesReport.txt");
		Scanner readingSalesFile = new Scanner(sales);
		String productLine = "";
		System.out.println("");
		System.out.println("||The Sales Report||");
		
		while(readingSalesFile.hasNextLine()) {
			productLine = readingSalesFile.nextLine();
			System.out.println(productLine);
		}
		readingSalesFile.close();
		displaySales.writeToActionLog();
	}
	
}
