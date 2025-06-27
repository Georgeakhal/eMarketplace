package com.example.eMarketplace.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
@ExtendWith(MockitoExtension.class)

class CredentialsValidatorTest {
    @Test
    void testValidate() {
        var username = "testUser";
        var email = "test@email.com";
        var password = "securePassword123";
        var birthday = Date.valueOf(LocalDate.of(2008, 2, 1));

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertTrue(result);
    }

    @Test
    void testNotValidate() {
        String username = null;
        String email = null;
        String password = null;
        Date birthday = null;

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertFalse(result);
    }

    @Test
    void testInvalidUsername() {
        var username = "test";
        var email = "test@email.com";
        var password = "securePassword123";
        var birthday = Date.valueOf(LocalDate.of(2008, 2, 1));

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertFalse(result);
    }

    @Test
    void testInvalidUsernameThatStartsWithNumber() {
        var username = "1test123456";
        var email = "test@email.com";
        var password = "securePassword123";
        var birthday = Date.valueOf(LocalDate.of(2008, 2, 1));

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertFalse(result);
    }

    @Test
    void testInvalidEmail() {
        var username = "testUser";
        var email = "invalid Email";
        var password = "securePassword123";
        var birthday = Date.valueOf(LocalDate.of(2008, 2, 1));

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertFalse(result);
    }

    @Test
    void testInvalidDate() {
        var username = "testUser";
        var email = "test@email.com";
        var password = "securePassword123";
        Date birthday = Date.valueOf(LocalDate.now());

        var result = CredentialsValidator.validate(username, email, password, birthday);

        assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null,test@email.com,password,2000-01-01",
            "testUser,test@email,password,1999-12-31",
            "testUser,test@email.com,,2022-05-15"
    }, nullValues = "null")
    void testValidateWithInvalidEmail(String username, String email, String password, String dateString) {
        Date date = dateString == null ? null : Date.valueOf(LocalDate.parse(dateString));
        var result = CredentialsValidator.validate(username, email, password, date);
        assertFalse(result);
    }
}

