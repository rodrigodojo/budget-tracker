package com.gestorgastos.persistence;

import com.gestorgastos.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String DATA_FILE = "gestor_gastos_data.txt";

    public void saveData(BudgetManager budgetManager) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println(budgetManager.getMonthlyIncome());
            
            for (Expense expense : budgetManager.getExpenses()) {
                writer.println(String.format("%s|%.2f|%s|%s",
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getCategory().name(),
                    expense.getDate().toString()
                ));
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public BudgetManager loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new BudgetManager(0);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String incomeLine = reader.readLine();
            if (incomeLine == null || incomeLine.trim().isEmpty()) {
                return new BudgetManager(0);
            }

            double income = Double.parseDouble(incomeLine.trim());
            BudgetManager budgetManager = new BudgetManager(income);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length != 4) continue;

                try {
                    String description = parts[0];
                    double amount = Double.parseDouble(parts[1].replace(",", "."));
                    Category category = Category.valueOf(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);

                    Expense expense = new Expense(description, amount, category, date);
                    budgetManager.addExpense(expense);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + line);
                }
            }

            return budgetManager;
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return new BudgetManager(0);
        }
    }

    public boolean dataFileExists() {
        return new File(DATA_FILE).exists();
    }
}
