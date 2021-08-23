package com.techelevator;

import com.techelevator.view.Auditor;
import com.techelevator.view.Menu;
import com.techelevator.view.SubMenu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineCLI {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

    Scanner userInput;
    private Menu menu;
    double currentBalance = 0.0;
    double deposit;
    String vMachine;


    // map to track quantity remaining
    Map<String, Integer> inventoryQuantityMap = new HashMap<String, Integer>();
    // map would have "c1" as key and {Cola, 1.25, drink} as value
    Map<String, String[]> inventoryMap = new HashMap<String, String[]>();


    public VendingMachineCLI(Menu menu) {
        this.menu = menu;
        this.userInput = new Scanner(System.in);
    }

    public VendingMachineCLI() {

    }

    public void run() {
        initializeMaps();
        while (true) {

            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                // display vending machine items
                mainMenuOption1();
            } else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
                // do purchase
                menuOption2();
            } else {
                System.out.println("Goodbye");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        VendingMachineCLI cli = new VendingMachineCLI(menu);
        cli.run();
    }

    public void mainMenuOption1() {
        //print out option list
        System.out.println("Here are your options: ");

        for (Map.Entry<String, String[]> product : inventoryMap.entrySet()) {
            // fetch the remaining quantity of the current product
            int isSoldOut = inventoryQuantityMap.get(product.getKey());
            // check that it is not sold out
            if (isSoldOut != 0) {

                System.out.printf("%s %s %s %s %s\n", product.getValue()[0], product.getValue()[1],
                        product.getValue()[2], product.getValue()[3], isSoldOut);
            } else {
                System.out.println("SOLD OUT");
            }
        }
    }

    public void menuOption2() {

        while (true) {
            SubMenu subMenu = new SubMenu(System.in, System.out);

            String subMenuOption1 = "Feed Money";
            String subMenuOption2 = "Select Product";
            String subMenuOption3 = "Finish Transaction";
            String[] subMenuOptions = {subMenuOption1, subMenuOption2, subMenuOption3};
            String subMenuChoice = (String) subMenu.getChoiceFromOptions(subMenuOptions, currentBalance);

            if (subMenuChoice.equals(subMenuOption1)) {
                subMenuOption1();
            } else if (subMenuChoice.equals(subMenuOption2)) {
                subMenuOption2();
            } else {
                subMenuOption3();
                break;

            }
        }
    }

    public void initializeMaps() {
        try (BufferedReader csvReader = new BufferedReader(new FileReader("vendingmachine.csv"))) {
            String line;
            while ((line = csvReader.readLine()) != null) {
                // Split each line at the pip to get value array
                String[] valuesArr = line.split("\\|");
                String key = valuesArr[0];

                inventoryQuantityMap.put(key, 5);
                inventoryMap.put(key, valuesArr);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String subMenuOption2() {
        for (Map.Entry<String, String[]> product : inventoryMap.entrySet()) {
            // fetch the remaining quantity of the current product
            int isSoldOut = inventoryQuantityMap.get(product.getKey());
            // check that it is not sold out
            if (isSoldOut != 0) {

                System.out.printf("%s %s %s %s\n", product.getValue()[0], product.getValue()[1],
                        product.getValue()[2], product.getValue()[3]);
            } else {
                System.out.println("SOLD OUT");
                return "SOLD OUT";
            }
        }
        System.out.print("Please select your snack: ");

        vMachine = userInput.nextLine().toUpperCase();    // take user input for product key
        if (!inventoryMap.containsKey(vMachine)) {
            System.out.println("Not a valid entry!");// IF invalid entry, inform user and return to submenu

        } else {
            int quantityRemaining = inventoryQuantityMap.get(vMachine);
            if (quantityRemaining == 0) { // check product availability
                System.out.println("We are sold out!");
                // IF sold out, inform user and return to submenu
            } else {
                if (Double.parseDouble(inventoryMap.get(vMachine)[2]) <= currentBalance) {
                    //price is the third thing, index 2 // IF sufficient balance (current balance > product cost)
                    deposit = currentBalance;
                    currentBalance -= Double.parseDouble(inventoryMap.get(vMachine)[2]);

                    String snackType = inventoryMap.get(vMachine)[3];
                    String snackMessage = "";
                    switch (snackType) {
                        case "Chip":
                            snackMessage = "Crunch Crunch, Yum!";
                            break;
                        case "Candy":
                            snackMessage = "Munch Munch, Yum!";
                            break;
                        case "Drink":
                            snackMessage = "Glug Glug, Yum!";
                            break;
                        default:
                            snackMessage = "Chew Chew, Yum!";
                            break;
                    }
                    System.out.println("Product: " + inventoryMap.get(vMachine)[1] + ", " +
                            "Price: " + inventoryMap.get(vMachine)[2] + ", " +
                            "Remaining balance: " + currentBalance);
                    System.out.println(snackMessage); // print product info, balance, and product message
                    quantityRemaining -= 1; // subtract from product quantity
                    inventoryQuantityMap.put(vMachine, quantityRemaining);
                    // int currQty = invMapQty.get(userInput) <- psuedocode
                    // currQty -= 1 ---> invMapQty.put(userInput, currQty)
                    Auditor.writeToLog(inventoryMap.get(vMachine)[1] + " " + vMachine + " \\$" + deposit + " \\$" + currentBalance);
                    return String.format("%s|%s|%s", inventoryMap.get(vMachine)[0], inventoryMap.get(vMachine)[1], inventoryMap.get(vMachine)[2]);
                } else {
                    System.out.println("Insufficient funds");
                }
            }
        }
        return "placeholder";
    }

    public void subMenuOption1() {
        System.out.print("Enter deposit amount (full dollar values only): ");
        String depositAsStr = userInput.nextLine();
        deposit = Double.parseDouble(depositAsStr);
        currentBalance += deposit;
        Auditor.writeToLog("FEED MONEY: \\$" + Double.toString(deposit) + " \\$" + Double.toString(currentBalance));

    }

    public int[] subMenuOption3() {
        deposit = currentBalance;
        int balanceAsCents = (int) (currentBalance * 100);
        int quarters = balanceAsCents / 25;
        balanceAsCents %= 25;
        int dimes = balanceAsCents / 10;
        balanceAsCents %= 10;
        int nickels = balanceAsCents / 5;
        balanceAsCents %= 5;
        currentBalance = balanceAsCents;
        int[] changeReturned = {balanceAsCents, quarters, dimes, nickels};

        System.out.printf("\nChange returned:\nQuarters: %d\nDimes: %d\nNickels: %d\n\n", quarters, dimes, nickels);
        Auditor.writeToLog("GIVE CHANGE: " + " \\$" + deposit + " \\$" + balanceAsCents);
        return changeReturned;

    }


}
