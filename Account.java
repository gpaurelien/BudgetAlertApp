package finanapp;

public class Account {
    private String id;
    private double budget;
    private double spent;

    public Account(String id, double budget) {
        this.id = id;
        this.budget = budget;
        this.spent = 0;
    }

    public String getId() { return id; }
    public double getBudget() { return budget; }
    public double getSpent() { return spent; }

    public void addTransaction(double amount) { spent += amount; }

    public boolean isOverBudget() { return spent > budget; }
    public boolean isNearLimit() { return spent >= budget * 0.8; }
}