import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String description;
    
    public Transaction(String transactionId, String accountNumber, String type, double amount, double balanceAfter, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void displayTransaction() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Type: " + type);
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Balance After: $" + String.format("%.2f", balanceAfter));
        System.out.println("Date: " + timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("Description: " + description);
        System.out.println("---------------------------------");
    }
    
    public String toFileString() {
        return transactionId + "," + accountNumber + "," + type + "," + amount + "," + balanceAfter + "," + timestamp.toString() + "," + description;
    }
    
    public static Transaction fromFileString(String fileString) {
        String[] parts = fileString.split(",", 7);
        Transaction transaction = new Transaction(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), parts[6]);
        transaction.timestamp = LocalDateTime.parse(parts[5]);
        return transaction;
    }
}

