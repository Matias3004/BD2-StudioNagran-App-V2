package com.studionagranapp.helpers.loginvalidation;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;

import java.sql.ResultSet;

public class UserInfoProvider {

    private Integer id;
    private String name;
    private String role;
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    public UserInfoProvider(String username, String password) {
        init(username, password);
    }

    private void init(String username, String password) {
        String getUserQuery = "SELECT * FROM User_accounts WHERE username = '" +
                username + "' AND password = '" + password + "'";
        try {
            ResultSet user = databaseManager.executeQuery(getUserQuery);
            while (user.next()) {
                this.id = user.getInt("account_id");
                this.name = user.getString("first_name") + " " + user.getString("last_name");
                this.role = user.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
