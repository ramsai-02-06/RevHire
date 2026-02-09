package com.revature.util;

import java.util.regex.Pattern;

public class InputValidator {

    // Email regex
	private static final String EMAIL =
		    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


    // Password regex:
    // At least 1 upper, 1 lower, 1 digit, 1 special char, min 8 chars
    private static final String PASSWORD =
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD, password);
    }
}
