package com.gestorgastos.model;

import java.time.LocalDate;
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

    public List<Expense> getExpensesByMonth(int month, int year) {
        return expenses.stream()
            .filter(e -> e.getDate().getMonthValue() == month && e.getDate().getYear() == year)
            .collect(Collectors.toList());
    }

    public double getTotalByCategory(Category category) {
        return expenses.stream()
            .filter(e -> e.getCategory() == category)
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getTotalByCategory(Category category, int month, int year) {
        if (month == 0 && year == 0) {
            return getTotalByCategory(category);
        }
        return expenses.stream()
            .filter(e -> e.getCategory() == category 
                      && e.getDate().getMonthValue() == month 
                      && e.getDate().getYear() == year)
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getRecommendedAmount(Category category) {
        return monthlyIncome * category.getPercentage() / 100.0;
    }

    public double getRemainingAmount(Category category) {
        return getRecommendedAmount(category) - getTotalByCategory(category);
    }

    public double getRemainingAmount(Category category, int month, int year) {
        return getRecommendedAmount(category) - getTotalByCategory(category, month, year);
    }

    public double getTotalExpenses() {
        return expenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getTotalExpenses(int month, int year) {
        if (month == 0 && year == 0) {
            return getTotalExpenses();
        }
        return expenses.stream()
            .filter(e -> e.getDate().getMonthValue() == month && e.getDate().getYear() == year)
            .mapToDouble(Expense::getAmount)
            .sum();
    }

    public double getTotalRemaining() {
        return monthlyIncome - getTotalExpenses();
    }

    public double getTotalRemaining(int month, int year) {
        return monthlyIncome - getTotalExpenses(month, year);
    }

    public boolean isOverBudget(Category category) {
        return getTotalByCategory(category) > getRecommendedAmount(category);
    }

    public boolean isOverBudget(Category category, int month, int year) {
        return getTotalByCategory(category, month, year) > getRecommendedAmount(category);
    }

    public double getUsagePercentage(Category category) {
        double recommended = getRecommendedAmount(category);
        if (recommended == 0) return 0;
        return (getTotalByCategory(category) / recommended) * 100;
    }

    public double getUsagePercentage(Category category, int month, int year) {
        double recommended = getRecommendedAmount(category);
        if (recommended == 0) return 0;
        return (getTotalByCategory(category, month, year) / recommended) * 100;
    }
}
