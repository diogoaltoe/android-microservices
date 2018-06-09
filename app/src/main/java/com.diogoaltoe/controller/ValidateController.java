package com.diogoaltoe.controller;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidateController {

    public ValidateController() {}

    public boolean isNameValid(String name) {
        return name.length() > 2;
    }

    public boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

}
