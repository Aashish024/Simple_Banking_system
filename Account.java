import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private String pin;
    private double balance;
    private LocalDateTime createdDate;
    
    public Account(String accountNumber, String accountHolderName, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.pin = pin;
        this.balance = initialBalance;
        this.createdDate = LocalDateTime.now();
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    public String getPin() {
        return pin;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public void displayAccountInfo() {
        System.out.println("=================================");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: $" + String.format("%.2f", balance));
        System.out.println("Created: " + createdDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        System.out.println("=================================");
    }
    
    public String toFileString() {
        return accountNumber + "," + accountHolderName + "," + pin + "," + balance + "," + createdDate.toString();
    }
    
    public static Account fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        Account account = new Account(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
        account.createdDate = LocalDateTime.parse(parts[4]);
        return account;
    }
}

