package finanapp;

import static spark.Spark.*;
import java.util.*;

public class BudgetAlertApp {
    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        port(4567);

        post("/createAccount", (req, res) -> {
            String id = req.queryParams("id");

            if (accounts.containsKey(id)) {
                return "This account ID is already used, sorry!";
            }

            double budget = Double.parseDouble(req.queryParams("budget"));
            accounts.put(id, new Account(id, budget));

            return id + " has been created successfully!";
        });

        get("/retrieveAccount", (req, res) -> {
            StringBuilder response = new StringBuilder();
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                response.append("Name: ").append(entry.getKey())
                        .append(", and his balance: ").append(entry.getValue().getBudget()).append("\n");
            }

            return response.toString();
        });

        delete("/deleteAccount", (req, res) -> {
            String id = req.queryParams("id");

            if (accounts.remove(id) == null) {
                res.status(404);
                return "Account not found!";
            }

            return "Account deleted, goodbye!";
        });

        post("/addTransaction", (req, res) -> {
            String id = req.queryParams("id");
            double amount = Double.parseDouble(req.queryParams("amount"));

            Account account = accounts.get(id);
            if (account == null) {
                res.status(404);
                return "Account not found!";
            }

            account.addTransaction(amount);

            if (account.isOverBudget()) {
                return "ALERT: budget exceeded!";
            } else if (account.isNearLimit()) {
                return "Warning: near budget limit!";
            }
            return "Transaction added. Total spent: " + account.getSpent();
        });

        get("/status/:id", (req, res) -> {
            // Same as retrieveAccount route, but for specified account
            String id = req.params("id");
            Account account = accounts.get(id);
            if (account == null) {
                res.status(404);
                return "Account not found!";
            }
            return String.format("Account %s - Budget: %.2f, Spent: %.2f",
                    id, account.getBudget(), account.getSpent());
        });
    }
}
