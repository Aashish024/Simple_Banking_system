import java.util.Scanner;

public class BankingSystem {
    private static BankManager bankManager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        bankManager = new BankManager();
        scanner = new Scanner(System.in);
        
        System.out.println("===========================================");
        System.out.println("       SIMPLE BANKING SYSTEM");
        System.out.println("       Developed by: Aashish Batra");
        System.out.println("===========================================");
        
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    bankManager.createAccount();
                    break;
                case 2:
                    bankManager.login();
                    break;
                case 3:
                    bankManager.displayAllAccounts();
                    break;
                case 4:
                    displayAbout();
                    break;
                case 5:
                    System.out.println("\nThank you for using Simple Banking System!");
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===========================================");
        System.out.println("           MAIN MENU");
        System.out.println("===========================================");
        System.out.println("1. Create New Account");
        System.out.println("2. Login to Account");
        System.out.println("3. View All Accounts (Admin)");
        System.out.println("4. About");
        System.out.println("5. Exit");
        System.out.println("===========================================");
        System.out.print("Enter your choice (1-5): ");
    }
    
    private static void displayAbout() {
        System.out.println("\n===========================================");
        System.out.println("           ABOUT THIS SYSTEM");
        System.out.println("===========================================");
        System.out.println("Simple Banking System v1.0");
        System.out.println("Developed by: Aashish Batra");
        System.out.println("Email: aashish.batra132@gmail.com");
        System.out.println("");
        System.out.println("Features:");
        System.out.println("- Create and manage bank accounts");
        System.out.println("- Secure PIN-based authentication");
        System.out.println("- Deposit and withdraw money");
        System.out.println("- Transfer money between accounts");
        System.out.println("- View transaction history");
        System.out.println("- Change account PIN");
        System.out.println("- Persistent data storage");
        System.out.println("===========================================");
    }
    
    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}

