package com.example.eMarketplace.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class CredentialsValidator {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{7,19}$");
    private final int minimumAge = 13;

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    public static boolean validate(String username, String email, String password, Date birthday) {

        System.out.println("→ VALIDATE username = " + username);
        System.out.println("→ VALIDATE email = " + email);
        System.out.println("→ VALIDATE password = " + password);
        System.out.println("→ VALIDATE birthday = " + birthday);

        return nameValidator(username) &&
                emailValidator(email) &&
                passwordValidator(password) &&
                birthdayValidator(birthday);
    }
    private boolean nameValidator(String username) {
        if (username == null || username.isBlank()) return false;
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        return matcher.matches();
    }

    private boolean emailValidator(String email){
        return email != null && !email.isBlank() && EMAIL_VALIDATOR.isValid(email);
    }

    private boolean passwordValidator(String password){
        return password != null && !password.isBlank();
    }

    private boolean birthdayValidator(Date birthday){
        if (birthday == null) {
            return false;
        }
        LocalDate birthLocalDate = birthday.toLocalDate();
        LocalDate today = LocalDate.now();

        int age = Period.between(birthLocalDate, today).getYears();
        return age >= minimumAge;
    }



}
