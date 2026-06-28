package com.gestorgastos.model;

public enum Category {
    GASTOS_FIXOS("Gastos Fixos", 50),
    GASTOS_NAO_ESSENCIAIS("Gastos Nao Essenciais", 30),
    INVESTIMENTO("Investimento", 20);

    private final String displayName;
    private final int percentage;

    Category(String displayName, int percentage) {
        this.displayName = displayName;
        this.percentage = percentage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return displayName + " (" + percentage + "%)";
    }
}
