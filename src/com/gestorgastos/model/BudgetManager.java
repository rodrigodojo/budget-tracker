package com.gestorgastos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {
    private double monthlyIncome;
    private List<Expense> expenses;

    public BudgetManager(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
        this.expenses = new ArrayList<>();
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    public double getTotalByCategory(Category category) {
        return expenses.stream()
            .filter(e -> e.getCategory() == category)
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getRecommendedAmount(Category category) {
        return monthlyIncome * category.getPercentage() / 100.0;
    }

    public double getRemainingAmount(Category category) {
        return getRecommendedAmount(category) - getTotalByCategory(category);
    }

    public double getTotalExpenses() {
        return expenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getTotalRemaining() {
        return monthlyIncome - getTotalExpenses();
    }

    public boolean isOverBudget(Category category) {
        return getTotalByCategory(category) > getRecommendedAmount(category);
    }

    public double getUsagePercentage(Category category) {
        double recommended = getRecommendedAmount(category);
        if (recommended == 0) return 0;
        return (getTotalByCategory(category) / recommended) * 100;
    }
}
