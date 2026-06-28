package com.gestorgastos.ui;

import com.gestorgastos.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainWindow extends JFrame {
    private BudgetManager budgetManager;
    private JTextField incomeField;
    private JLabel totalIncomeLabel;
    private JLabel totalExpensesLabel;
    private JLabel totalRemainingLabel;
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private JProgressBar[] categoryProgressBars;
    private JLabel[] categoryLabels;
    private JLabel[] categoryAmountLabels;

    public MainWindow() {
        budgetManager = new BudgetManager(0);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Gestor de Gastos - Regra 50/30/20");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 700);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Configuração de Renda"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Renda Mensal (R$):"));
        incomeField = new JTextField(10);
        inputPanel.add(incomeField);
        JButton setIncomeButton = new JButton("Definir Renda");
        setIncomeButton.addActionListener(e -> setMonthlyIncome());
        inputPanel.add(setIncomeButton);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        totalIncomeLabel = new JLabel("Renda: R$ 0,00");
        totalExpensesLabel = new JLabel("Gastos: R$ 0,00");
        totalRemainingLabel = new JLabel("Restante: R$ 0,00");

        totalIncomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalExpensesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalRemainingLabel.setFont(new Font("Arial", Font.BOLD, 14));

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpensesLabel);
        summaryPanel.add(totalRemainingLabel);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(summaryPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel categoriesPanel = createCategoriesPanel();
        JPanel expensesPanel = createExpensesPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, categoriesPanel, expensesPanel);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.4);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Categorias - Regra 50/30/20"));

        Category[] categories = Category.values();
        categoryProgressBars = new JProgressBar[categories.length];
        categoryLabels = new JLabel[categories.length];
        categoryAmountLabels = new JLabel[categories.length];

        for (int i = 0; i < categories.length; i++) {
            JPanel categoryPanel = createCategoryPanel(categories[i], i);
            panel.add(categoryPanel);
        }

        return panel;
    }

    private JPanel createCategoryPanel(Category category, int index) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setBackground(getCategoryColor(category));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        categoryLabels[index] = new JLabel(category.toString());
        categoryLabels[index].setFont(new Font("Arial", Font.BOLD, 12));

        categoryAmountLabels[index] = new JLabel("R$ 0,00 / R$ 0,00");
        categoryAmountLabels[index].setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(categoryLabels[index], BorderLayout.WEST);
        topPanel.add(categoryAmountLabels[index], BorderLayout.EAST);

        categoryProgressBars[index] = new JProgressBar(0, 100);
        categoryProgressBars[index].setStringPainted(true);
        categoryProgressBars[index].setString("0%");

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(categoryProgressBars[index], BorderLayout.CENTER);

        return panel;
    }

    private Color getCategoryColor(Category category) {
        switch (category) {
            case GASTOS_FIXOS:
                return new Color(255, 200, 200);
            case GASTOS_NAO_ESSENCIAIS:
                return new Color(255, 255, 200);
            case INVESTIMENTO:
                return new Color(200, 255, 200);
            default:
                return Color.WHITE;
        }
    }

    private JPanel createExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Gastos Registrados"));

        String[] columnNames = {"Descrição", "Valor (R$)", "Categoria", "Data"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expensesTable = new JTable(tableModel);
        expensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(expensesTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addExpenseButton = new JButton("Adicionar Gasto");
        addExpenseButton.setFont(new Font("Arial", Font.BOLD, 14));
        addExpenseButton.setBackground(new Color(100, 200, 100));
        addExpenseButton.setForeground(Color.WHITE);
        addExpenseButton.setFocusPainted(false);
        addExpenseButton.addActionListener(e -> showAddExpenseDialog());

        JButton removeExpenseButton = new JButton("Remover Gasto");
        removeExpenseButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeExpenseButton.setBackground(new Color(200, 100, 100));
        removeExpenseButton.setForeground(Color.WHITE);
        removeExpenseButton.setFocusPainted(false);
        removeExpenseButton.addActionListener(e -> removeSelectedExpense());

        panel.add(addExpenseButton);
        panel.add(removeExpenseButton);

        return panel;
    }

    private void setMonthlyIncome() {
        try {
            double income = Double.parseDouble(incomeField.getText().replace(",", "."));
            if (income <= 0) {
                JOptionPane.showMessageDialog(this, "A renda deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            budgetManager.setMonthlyIncome(income);
            updateUI();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddExpenseDialog() {
        if (budgetManager.getMonthlyIncome() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, defina sua renda mensal primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Adicionar Gasto", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JTextField descriptionField = new JTextField();
        JTextField amountField = new JTextField();
        JComboBox<Category> categoryCombo = new JComboBox<>(Category.values());
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        dialog.add(new JLabel("Descrição:"));
        dialog.add(descriptionField);
        dialog.add(new JLabel("Valor (R$):"));
        dialog.add(amountField);
        dialog.add(new JLabel("Categoria:"));
        dialog.add(categoryCombo);
        dialog.add(new JLabel("Data (AAAA-MM-DD):"));
        dialog.add(dateField);

        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            try {
                String description = descriptionField.getText().trim();
                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Descrição não pode ser vazia!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountField.getText().replace(",", "."));
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Valor deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Category category = (Category) categoryCombo.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText());

                Expense expense = new Expense(description, amount, category, date);
                budgetManager.addExpense(expense);
                updateUI();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato AAAA-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void removeSelectedExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gasto para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este gasto?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Expense expense = budgetManager.getExpenses().get(selectedRow);
            budgetManager.removeExpense(expense);
            updateUI();
        }
    }

    private void updateUI() {
        totalIncomeLabel.setText(String.format("Renda: R$ %.2f", budgetManager.getMonthlyIncome()));
        totalExpensesLabel.setText(String.format("Gastos: R$ %.2f", budgetManager.getTotalExpenses()));
        totalRemainingLabel.setText(String.format("Restante: R$ %.2f", budgetManager.getTotalRemaining()));

        if (budgetManager.getTotalRemaining() < 0) {
            totalRemainingLabel.setForeground(Color.RED);
        } else {
            totalRemainingLabel.setForeground(new Color(0, 128, 0));
        }

        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            double spent = budgetManager.getTotalByCategory(category);
            double recommended = budgetManager.getRecommendedAmount(category);
            double percentage = budgetManager.getUsagePercentage(category);

            categoryAmountLabels[i].setText(String.format("R$ %.2f / R$ %.2f", spent, recommended));
            categoryProgressBars[i].setValue((int) Math.min(percentage, 100));
            categoryProgressBars[i].setString(String.format("%.1f%%", percentage));

            if (budgetManager.isOverBudget(category)) {
                categoryProgressBars[i].setForeground(Color.RED);
            } else if (percentage > 80) {
                categoryProgressBars[i].setForeground(Color.ORANGE);
            } else {
                categoryProgressBars[i].setForeground(new Color(0, 150, 0));
            }
        }

        tableModel.setRowCount(0);
        for (Expense expense : budgetManager.getExpenses()) {
            tableModel.addRow(new Object[]{
                expense.getDescription(),
                String.format("%.2f", expense.getAmount()),
                expense.getCategory().getDisplayName(),
                expense.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
