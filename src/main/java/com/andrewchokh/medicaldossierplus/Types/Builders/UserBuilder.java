package com.andrewchokh.medicaldossierplus.Types.Builders;

import com.andrewchokh.medicaldossierplus.Types.User;

public class UserBuilder {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int accountType;

    public UserBuilder id(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder accountType(int accountType) {
        this.accountType = accountType;
        return this;
    }

    public User build() {
        return new User(
            id, firstName, lastName, email, password, accountType
        );
    }
}
