package com.andrewchokh.medicaldossierplus.Enums;

public enum AccountTypes {
    REGULAR(0, "Regular"),
    ADMIN(1, "Admin");

    private final int accountType;
    private final String typeName;

    AccountTypes(int accountType, String typeName) {
        this.accountType = accountType;
        this.typeName = typeName;
    }

    public int getValue() {
        return accountType;
    }

    @Override
    public String toString() { return typeName; }
}
