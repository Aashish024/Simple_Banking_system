import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class BankManager {
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;
    private Scanner scanner;
    private final String ACCOUNTS_FILE = "accounts.txt";
    private final String TRANSACTIONS_FILE = "transactions.txt";
    private int transactionCounter;
    
    public BankManager() {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
        transactionCounter = 1;
        loadAccountsFromFile();
        loadTransactionsFromFile();
    }
    
    public void createAccount() {
        System.out.println("\n--- Create New Account ---");
        
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        if (findAccountByNumber(accountNumber) != null) {
            System.out.println("Error: Account with number " + accountNumber + " already exists!");
            return;
        }
        
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Create 4-digit PIN: ");
        String pin = scanner.nextLine();
        
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            System.out.println("Error: PIN must be exactly 4 digits!");
            return;
        }
        
        System.out.print("Enter Initial Deposit: $");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();
        
        if (initialDeposit < 0) {
            System.out.println("Error: Initial deposit cannot be negative!");
            return;
        }
        
        Account newAccount = new Account(accountNumber, name, pin, initialDeposit);
        accounts.add(newAccount);
        saveAccountsToFile();
        
        if (initialDeposit > 0) {
            addTransaction(accountNumber, "DEPOSIT", initialDeposit, initialDeposit, "Initial deposit");
        }
        
        System.out.println("Account created successfully!");
    }
    
    public void login() {
        System.out.println("\n--- Account Login ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        
        Account account = findAccountByNumber(accountNumber);
        if (account != null && account.getPin().equals(pin)) {
            System.out.println("Login successful!");
            accountMenu(account);
        } else {
            System.out.println("Invalid account number or PIN!");
        }
    }
    
    private void accountMenu(Account account) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n===========================================");
            System.out.println("Welcome, " + account.getAccountHolderName());
            System.out.println("===========================================");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Change PIN");
            System.out.println("7. Logout");
            System.out.println("===========================================");
            System.out.print("Enter your choice (1-7): ");
            
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    checkBalance(account);
                    break;
                case 2:
                    depositMoney(account);
                    break;
                case 3:
                    withdrawMoney(account);
                    break;
                case 4:
                    transferMoney(account);
                    break;
                case 5:
                    viewTransactionHistory(account);
                    break;
                case 6:
                    changePIN(account);
                    break;
                case 7:
                    System.out.println("Logged out successfully!");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (loggedIn) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void checkBalance(Account account) {
        System.out.println("\n--- Account Balance ---");
        account.displayAccountInfo();
    }
    
    private void depositMoney(Account account) {
        System.out.println("\n--- Deposit Money ---");
        System.out.print("Enter amount to deposit: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        if (account.deposit(amount)) {
            saveAccountsToFile();
            addTransaction(account.getAccountNumber(), "DEPOSIT", amount, account.getBalance(), "Cash deposit");
            System.out.println("Deposit successful! New balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }
    
    private void withdrawMoney(Account account) {
        System.out.println("\n--- Withdraw Money ---");
        System.out.print("Enter amount to withdraw: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        if (account.withdraw(amount)) {
            saveAccountsToFile();
            addTransaction(account.getAccountNumber(), "WITHDRAWAL", amount, account.getBalance(), "Cash withdrawal");
            System.out.println("Withdrawal successful! New balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Insufficient funds or invalid amount!");
        }
    }
    
    private void transferMoney(Account account) {
        System.out.println("\n--- Transfer Money ---");
        System.out.print("Enter recipient account number: ");
        String recipientAccountNumber = scanner.nextLine();
        
        Account recipientAccount = findAccountByNumber(recipientAccountNumber);
        if (recipientAccount == null) {
            System.out.println("Recipient account not found!");
            return;
        }
        
        if (recipientAccount.getAccountNumber().equals(account.getAccountNumber())) {
            System.out.println("Cannot transfer to the same account!");
            return;
        }
        
        System.out.print("Enter amount to transfer: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        if (account.withdraw(amount)) {
            recipientAccount.deposit(amount);
            saveAccountsToFile();
            
            addTransaction(account.getAccountNumber(), "TRANSFER_OUT", amount, account.getBalance(), "Transfer to " + recipientAccountNumber);
            addTransaction(recipientAccountNumber, "TRANSFER_IN", amount, recipientAccount.getBalance(), "Transfer from " + account.getAccountNumber());
            
            System.out.println("Transfer successful!");
            System.out.println("Transferred $" + String.format("%.2f", amount) + " to " + recipientAccount.getAccountHolderName());
            System.out.println("New balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Insufficient funds or invalid amount!");
        }
    }
    
    private void viewTransactionHistory(Account account) {
        System.out.println("\n--- Transaction History ---");
        
        ArrayList<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(account.getAccountNumber())) {
                accountTransactions.add(transaction);
            }
        }
        
        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions found!");
            return;
        }
        
        System.out.println("Recent transactions for account: " + account.getAccountNumber());
        System.out.println("=================================");
        
        for (int i = accountTransactions.size() - 1; i >= Math.max(0, accountTransactions.size() - 10); i--) {
            accountTransactions.get(i).displayTransaction();
        }
    }
    
    private void changePIN(Account account) {
        System.out.println("\n--- Change PIN ---");
        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine();
        
        if (!account.getPin().equals(currentPin)) {
            System.out.println("Incorrect current PIN!");
            return;
        }
        
        System.out.print("Enter new 4-digit PIN: ");
        String newPin = scanner.nextLine();
        
        if (newPin.length() != 4 || !newPin.matches("\\d+")) {
            System.out.println("Error: PIN must be exactly 4 digits!");
            return;
        }
        
        account.setPin(newPin);
        saveAccountsToFile();
        System.out.println("PIN changed successfully!");
    }
    
    public void displayAllAccounts() {
        System.out.println("\n--- All Accounts ---");
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found!");
            return;
        }
        
        for (Account account : accounts) {
            account.displayAccountInfo();
        }
        
        System.out.println("Total Accounts: " + accounts.size());
    }
    
    private Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    private void addTransaction(String accountNumber, String type, double amount, double balanceAfter, String description) {
        String transactionId = "TXN" + String.format("%06d", transactionCounter++);
        Transaction transaction = new Transaction(transactionId, accountNumber, type, amount, balanceAfter, description);
        transactions.add(transaction);
        saveTransactionsToFile();
    }
    
    private int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
    
    private void saveAccountsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts) {
                writer.println(account.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
    
    private void loadAccountsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Account account = Account.fromFileString(line);
                    accounts.add(account);
                }
            }
            System.out.println("Loaded " + accounts.size() + " accounts from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing accounts file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
    
    private void saveTransactionsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    private void loadTransactionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Transaction transaction = Transaction.fromFileString(line);
                    transactions.add(transaction);
                    
                    String txnId = transaction.getTransactionId();
                    if (txnId.startsWith("TXN")) {
                        int id = Integer.parseInt(txnId.substring(3));
                        if (id >= transactionCounter) {
                            transactionCounter = id + 1;
                        }
                    }
                }
            }
            System.out.println("Loaded " + transactions.size() + " transactions from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing transactions file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
    
    public int getTotalAccounts() {
        return accounts.size();
    }
}

