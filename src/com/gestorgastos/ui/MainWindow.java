package com.gestorgastos.ui;

import com.gestorgastos.model.*;
import com.gestorgastos.persistence.DataManager;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainWindow extends JFrame {
    private BudgetManager budgetManager;
    private DataManager dataManager;
    private JTextField incomeField;
    
    // Novas referências de painéis e componentes modernos
    private MetricCard incomeMetricCard;
    private MetricCard expensesMetricCard;
    private MetricCard remainingMetricCard;
    
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private JProgressBar[] categoryProgressBars;
    private JLabel[] categoryLabels;
    private JLabel[] categoryAmountLabels;

    // Paleta de Cores Estilo Tailwind / Slate Moderno
    private static final Color COLOR_BG = new Color(248, 250, 252);       // Slate 50
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(226, 232, 240);    // Slate 200
    private static final Color COLOR_TEXT_PRIMARY = new Color(30, 41, 59);  // Slate 800
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139); // Slate 500
    
    private static final Color COLOR_PRIMARY = new Color(79, 70, 229);      // Indigo 600
    private static final Color COLOR_PRIMARY_HOVER = new Color(67, 56, 202); // Indigo 700

    private static final Color COLOR_FIXED_EXPENSE = new Color(59, 130, 246); // Blue 500
    private static final Color COLOR_WANTED_EXPENSE = new Color(245, 158, 11); // Amber 500
    private static final Color COLOR_INVESTMENT = new Color(16, 185, 129);   // Emerald 500
    private static final Color COLOR_OVER_BUDGET = new Color(239, 68, 68);   // Red 500

    public MainWindow() {
        dataManager = new DataManager();
        budgetManager = dataManager.loadData();
        setupGlobalFonts();
        initializeUI();
        updateUI();
    }

    private void setupGlobalFonts() {
        // Tenta usar Segoe UI por padrão para um visual moderno
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
    }

    private void initializeUI() {
        setTitle("Gestor de Gastos - Regra 50/30/20");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(950, 750);
        getContentPane().setBackground(COLOR_BG);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });

        
        createMenuBar();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(COLOR_CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        menuBar.setPreferredSize(new Dimension(0, 40));

        
        JMenu fileMenu = createStyledMenu("Arquivo");
        JMenu helpMenu = createStyledMenu("Ajuda");

        
        JMenuItem exitItem = createStyledMenuItem("Sair de Aplicação");
        exitItem.addActionListener(e -> saveAndExit());

        
        JMenuItem aboutItem = createStyledMenuItem("Sobre o Software");
        aboutItem.addActionListener(e -> showAboutDialog());

        fileMenu.add(exitItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setForeground(COLOR_TEXT_PRIMARY);
        menu.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        
        menu.getPopupMenu().setBorder(new LineBorder(COLOR_BORDER, 1));
        menu.getPopupMenu().setBackground(COLOR_CARD_BG);
        return menu;
    }

    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setForeground(COLOR_TEXT_PRIMARY);
        item.setBackground(COLOR_CARD_BG);
        item.setBorder(new EmptyBorder(8, 15, 8, 15));
        item.setOpaque(true);

        
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(241, 245, 249)); 
                item.setForeground(COLOR_PRIMARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(COLOR_CARD_BG);
                item.setForeground(COLOR_TEXT_PRIMARY);
            }
        });
        return item;
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "Sobre o Gestor de Gastos", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(440, 340);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_CARD_BG);

        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(25, 25, 15, 25));

        
        JLabel titleLabel = new JLabel("GESTOR DE GASTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Regra de Orçamento 50/30/20");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitleLabel.setForeground(COLOR_TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(380, 2));
        separator.setForeground(COLOR_BORDER);
        separator.setBackground(COLOR_BORDER);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        String descHtml = "<html><body style='text-align: center; font-family: \"Segoe UI\"; font-size: 11px; color: #1E293B;'>"
            + "<p style='margin-bottom: 8px;'>Uma aplicação financeira moderna para planejamento de gastos mensais,</p>"
            + "<p style='margin-bottom: 8px;'>dividindo inteligentemente seus recursos entre <b>Necessidades (50%)</b>,</p>"
            + "<p style='margin-bottom: 8px;'><b>Desejos Pessoais (30%)</b> e <b>Investimentos (20%)</b>.</p>"
            + "<br>"
            + "<p style='color: #64748B;'>Desenvolvido por: <b style='color: #4F46E5;'>rodrigodojo</b></p>"
            + "<p style='color: #64748B; margin-top: 4px;'>Versão 2.0.0 • 2026</p>"
            + "</body></html>";
        
        JLabel descLabel = new JLabel(descHtml);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainContent.add(titleLabel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 4)));
        mainContent.add(subtitleLabel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(separator);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(descLabel);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton closeButton = new ModernButton("Fechar", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        closeButton.setPreferredSize(new Dimension(120, 36));
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        dialog.add(mainContent, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        container.setOpaque(false);

        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);

        
        JPanel inputCard = new JPanel(new BorderLayout(8, 8));
        inputCard.setBackground(COLOR_CARD_BG);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel inputLabel = new JLabel("Definir Renda Mensal");
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        inputLabel.setForeground(COLOR_TEXT_MUTED);
        
        JPanel formPanel = new JPanel(new BorderLayout(8, 0));
        formPanel.setOpaque(false);
        
        incomeField = new JTextField(8);
        incomeField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        
        JButton setIncomeButton = new ModernButton("Definir", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        setIncomeButton.addActionListener(e -> setMonthlyIncome());
        
        formPanel.add(incomeField, BorderLayout.CENTER);
        formPanel.add(setIncomeButton, BorderLayout.EAST);
        
        inputCard.add(inputLabel, BorderLayout.NORTH);
        inputCard.add(formPanel, BorderLayout.CENTER);

        
        incomeMetricCard = new MetricCard("RENDA MENSAL", "R$ 0,00", COLOR_PRIMARY);
        expensesMetricCard = new MetricCard("GASTOS TOTAIS", "R$ 0,00", COLOR_WANTED_EXPENSE);
        remainingMetricCard = new MetricCard("SALDO RESTANTE", "R$ 0,00", COLOR_INVESTMENT);

        cardsPanel.add(inputCard);
        cardsPanel.add(incomeMetricCard);
        cardsPanel.add(expensesMetricCard);
        cardsPanel.add(remainingMetricCard);

        container.add(cardsPanel, BorderLayout.CENTER);
        return container;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        JPanel categoriesPanel = createCategoriesPanel();
        JPanel expensesPanel = createExpensesPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, categoriesPanel, expensesPanel);
        splitPane.setDividerLocation(260);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
        splitPane.setBackground(COLOR_BG);
        splitPane.setOpaque(true);
        splitPane.setResizeWeight(0.35);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCategoriesPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("Distribuição de Orçamento (Regra 50/30/20)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_PRIMARY);
        container.add(sectionTitle, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setOpaque(false);

        Category[] categories = Category.values();
        categoryProgressBars = new JProgressBar[categories.length];
        categoryLabels = new JLabel[categories.length];
        categoryAmountLabels = new JLabel[categories.length];

        for (int i = 0; i < categories.length; i++) {
            JPanel categoryCard = createCategoryCard(categories[i], i);
            panel.add(categoryCard);
        }

        container.add(panel, BorderLayout.CENTER);
        return container;
    }

    private JPanel createCategoryCard(Category category, int index) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);

        categoryLabels[index] = new JLabel(category.getDisplayName() + " (" + category.getPercentage() + "%)");
        categoryLabels[index].setFont(new Font("Segoe UI", Font.BOLD, 13));
        categoryLabels[index].setForeground(COLOR_TEXT_PRIMARY);

        categoryAmountLabels[index] = new JLabel("R$ 0,00 / R$ 0,00");
        categoryAmountLabels[index].setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryAmountLabels[index].setForeground(COLOR_TEXT_MUTED);

        labelPanel.add(categoryLabels[index], BorderLayout.NORTH);
        labelPanel.add(categoryAmountLabels[index], BorderLayout.SOUTH);

        categoryProgressBars[index] = new ModernProgressBar();
        categoryProgressBars[index].setMinimum(0);
        categoryProgressBars[index].setMaximum(100);
        categoryProgressBars[index].setValue(0);
        categoryProgressBars[index].setStringPainted(true);
        categoryProgressBars[index].setString("0.0%");

        card.add(labelPanel, BorderLayout.NORTH);
        card.add(categoryProgressBars[index], BorderLayout.CENTER);

        return card;
    }

    private JPanel createExpensesPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("Histórico de Gastos Registrados");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_PRIMARY);
        container.add(sectionTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"Descrição", "Valor", "Categoria", "Data"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expensesTable = new JTable(tableModel);
        expensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expensesTable.setRowHeight(32);
        expensesTable.setGridColor(COLOR_BG);
        expensesTable.setShowVerticalLines(false);
        expensesTable.setShowHorizontalLines(true);
        expensesTable.setSelectionBackground(new Color(224, 231, 255)); 
        expensesTable.setSelectionForeground(COLOR_TEXT_PRIMARY);

        
        JTableHeader header = expensesTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(new Color(71, 85, 105));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        expensesTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer); 
        expensesTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); 
        expensesTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer); 
        expensesTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); 

        JScrollPane scrollPane = new JScrollPane(expensesTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_CARD_BG);

        card.add(scrollPane, BorderLayout.CENTER);
        container.add(card, BorderLayout.CENTER);

        return container;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        JButton addExpenseButton = new ModernButton("Adicionar Gasto", new Color(16, 185, 129), new Color(5, 150, 105));
        addExpenseButton.setPreferredSize(new Dimension(180, 42));
        addExpenseButton.addActionListener(e -> showAddExpenseDialog());

        JButton removeExpenseButton = new ModernButton("Remover Gasto", new Color(239, 68, 68), new Color(220, 38, 38));
        removeExpenseButton.setPreferredSize(new Dimension(180, 42));
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
            dataManager.saveData(budgetManager);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor valido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAndExit() {
        dataManager.saveData(budgetManager);
        System.exit(0);
    }

    private void showAddExpenseDialog() {
        if (budgetManager.getMonthlyIncome() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, defina sua renda mensal primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Adicionar Gasto", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JTextField descriptionField = new JTextField();
        descriptionField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JTextField amountField = new JTextField();
        amountField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JComboBox<Category> categoryCombo = new JComboBox<>(Category.values());
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryCombo.setBackground(Color.WHITE);

        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JLabel descLabel = new JLabel("Descrição:");
        descLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel valLabel = new JLabel("Valor (R$):");
        valLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel catLabel = new JLabel("Categoria:");
        catLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel datLabel = new JLabel("Data (AAAA-MM-DD):");
        datLabel.setForeground(COLOR_TEXT_PRIMARY);

        contentPanel.add(descLabel);
        contentPanel.add(descriptionField);
        contentPanel.add(valLabel);
        contentPanel.add(amountField);
        contentPanel.add(catLabel);
        contentPanel.add(categoryCombo);
        contentPanel.add(datLabel);
        contentPanel.add(dateField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 20, 15, 20));

        JButton saveButton = new ModernButton("Salvar", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        saveButton.setPreferredSize(new Dimension(100, 36));

        JButton cancelButton = new ModernButton("Cancelar", new Color(148, 163, 184), new Color(100, 116, 139));
        cancelButton.setPreferredSize(new Dimension(100, 36));

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
                dataManager.saveData(budgetManager);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato AAAA-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void removeSelectedExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gasto para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este gasto?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Expense expense = budgetManager.getExpenses().get(selectedRow);
            budgetManager.removeExpense(expense);
            updateUI();
            dataManager.saveData(budgetManager);
        }
    }

    private void updateUI() {
        double monthlyIncome = budgetManager.getMonthlyIncome();
        if (monthlyIncome > 0 && incomeField != null) {
            incomeField.setText(String.format("%.2f", monthlyIncome));
        }
        
        if (incomeMetricCard != null) {
            incomeMetricCard.setValue(String.format("R$ %.2f", monthlyIncome));
        }
        if (expensesMetricCard != null) {
            double totalExpenses = budgetManager.getTotalExpenses();
            expensesMetricCard.setValue(String.format("R$ %.2f", totalExpenses));
        }
        if (remainingMetricCard != null) {
            double totalRemaining = budgetManager.getTotalRemaining();
            remainingMetricCard.setValue(String.format("R$ %.2f", totalRemaining));
            if (totalRemaining < 0) {
                remainingMetricCard.setIndicatorColor(COLOR_OVER_BUDGET);
                remainingMetricCard.setValueColor(COLOR_OVER_BUDGET);
            } else {
                remainingMetricCard.setIndicatorColor(COLOR_INVESTMENT);
                remainingMetricCard.setValueColor(COLOR_INVESTMENT);
            }
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

            Color barColor;
            if (budgetManager.isOverBudget(category)) {
                barColor = COLOR_OVER_BUDGET;
            } else {
                switch (category) {
                    case GASTOS_FIXOS:
                        barColor = COLOR_FIXED_EXPENSE;
                        break;
                    case GASTOS_NAO_ESSENCIAIS:
                        barColor = COLOR_WANTED_EXPENSE;
                        break;
                    case INVESTIMENTO:
                        barColor = COLOR_INVESTMENT;
                        break;
                    default:
                        barColor = COLOR_PRIMARY;
                }
            }
            categoryProgressBars[i].setForeground(barColor);
        }

        tableModel.setRowCount(0);
        for (Expense expense : budgetManager.getExpenses()) {
            tableModel.addRow(new Object[]{
                expense.getDescription(),
                String.format("R$ %.2f", expense.getAmount()),
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

    
    public static class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;

        public ModernButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    repaint();
                }
            });
            setBackground(normalColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    
    public static class MetricCard extends JPanel {
        private String title;
        private String value;
        private Color indicatorColor;
        private JLabel valueLabel;
        private JPanel indicatorPanel;

        public MetricCard(String title, String value, Color indicatorColor) {
            this.title = title;
            this.value = value;
            this.indicatorColor = indicatorColor;

            setLayout(new BorderLayout(10, 5));
            setBackground(COLOR_CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(12, 15, 12, 15)
            ));

            indicatorPanel = new JPanel();
            indicatorPanel.setPreferredSize(new Dimension(4, 0));
            indicatorPanel.setBackground(indicatorColor);

            JPanel textPanel = new JPanel(new BorderLayout(0, 4));
            textPanel.setOpaque(false);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            titleLabel.setForeground(COLOR_TEXT_MUTED);

            valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            valueLabel.setForeground(COLOR_TEXT_PRIMARY);

            textPanel.add(titleLabel, BorderLayout.NORTH);
            textPanel.add(valueLabel, BorderLayout.CENTER);

            add(indicatorPanel, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
        }

        public void setValue(String value) {
            this.value = value;
            if (valueLabel != null) {
                valueLabel.setText(value);
            }
        }

        public void setValueColor(Color color) {
            if (valueLabel != null) {
                valueLabel.setForeground(color);
            }
        }

        public void setIndicatorColor(Color color) {
            this.indicatorColor = color;
            if (indicatorPanel != null) {
                indicatorPanel.setBackground(color);
                indicatorPanel.repaint();
            }
        }
    }

    
    public static class ModernProgressBar extends JProgressBar {
        public ModernProgressBar() {
            setOpaque(false);
            setBorder(null);
            setBackground(new Color(241, 245, 249)); 
        }

        @Override
        public void setForeground(Color c) {
            super.setForeground(c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, 8, 8));

            
            double percentage = getPercentComplete();
            int progressWidth = (int) (width * percentage);

            g2.setColor(getForeground());
            if (progressWidth > 0) {
                g2.fill(new RoundRectangle2D.Double(0, 0, progressWidth, height, 8, 8));
            }

            
            if (isStringPainted() && getString() != null) {
                g2.setColor(COLOR_TEXT_PRIMARY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(getString())) / 2;
                int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getString(), x, y);
            }

            g2.dispose();
        }
    }
}
