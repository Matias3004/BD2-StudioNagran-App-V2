package com.studionagranapp.helpers.loginvalidation;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginValidator implements LoginValidation {

    private final String username;
    private final String password;
    private final DatabaseManager databaseManager;

    public LoginValidator(String username, String password) {
        this.username = username;
        this.password = password;
        this.databaseManager = DatabaseManager.getInstance();
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.INVALID_LOGIN;

        String verifyLogin = "SELECT count(1) FROM BD2_Projekt_StudioNagran.users_accounts WHERE username = '"
                + this.username + "' AND password = '" + this.password + "'";

        ResultSet queryResult = databaseManager.executeQuery(verifyLogin);
        try {
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1)
                    return ValidationResult.AUTHORIZATION_OBTAINED;
            }
        } catch (SQLException t) {
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return result;
    }
}
