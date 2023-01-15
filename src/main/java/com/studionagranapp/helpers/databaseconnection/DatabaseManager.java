package com.studionagranapp.helpers.databaseconnection;

import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Session;
import com.studionagranapp.helpers.query.QueryExecutor;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private final DatabaseConnector databaseConnector;
    private final QueryExecutor queryExecutor;
    private final AlertManager alertManager;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DatabaseManager() {
        databaseConnector = new DatabaseConnector();
        queryExecutor = new QueryExecutor();
        alertManager = new AlertManager();
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null)
            databaseManager = new DatabaseManager();

        return databaseManager;
    }

    public ResultSet executeQuery(String query) {
        return queryExecutor.executeQuery(query);
    }

    public Connection getConnection() {
        return databaseConnector.connect();
    }

    private DatabaseResponse performDeletion(String query) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.execute();

            return DatabaseResponse.SUCCESS;
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();

            return DatabaseResponse.ERROR;
        }
    }

    public DatabaseResponse insertClient(String first_name, String last_name, String username, String password, String role, String email, String phone_number) {
        String query = "SELECT * FROM User_accounts";
        ResultSet usernames = queryExecutor.executeQuery(query);

        try {
            while (usernames.next()) {
                if (Objects.equals(usernames.getString("username"), username))
                    return DatabaseResponse.USERNAME_ALREADY_EXIST;
            }

            String addClientQuery = "INSERT INTO User_accounts (first_name, last_name, username, password, role, email, phone_number) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement preparedClient = getConnection().prepareStatement(addClientQuery);

                return getDatabaseResponse(first_name, last_name, username, password, role, email, phone_number, preparedClient);

            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();

                return DatabaseResponse.ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();

            return DatabaseResponse.ERROR;
        }
    }

    public DatabaseResponse insertSession(String sessionName, String bandName, LocalDate startDate, LocalDate endDate, Integer Clients_id, String engineer) {
        String engineerFirstName = engineer.split(" ")[0];
        String engineerLastName = engineer.split(" ")[1];

        String getEngineerIdQuery = "SELECT account_id FROM user_accounts " +
                "WHERE first_name = '" + engineerFirstName +
                "' AND last_name = '" + engineerLastName + "'";
        ResultSet engineerID = queryExecutor.executeQuery(getEngineerIdQuery);

        Integer duration = 8 * (int) ChronoUnit.DAYS.between(startDate, endDate);
        Timestamp start_date = Timestamp.valueOf(startDate + " 10:00:00");
        Timestamp end_date = Timestamp.valueOf(endDate + " 18:00:00");

        try {
            Integer Engineer_id = 0;
            if (engineerID.next()) {
                Engineer_id = engineerID.getInt("account_id");
            }

            String addSessionQuery = "INSERT INTO Sessions (session_name, band_name, start_date, end_date, duration, Clients_id, Engineer_id) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement preparedSession = getConnection().prepareStatement(addSessionQuery);

                return getDatabaseResponse(sessionName, bandName, start_date, end_date, duration, Clients_id, Engineer_id, preparedSession);
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();

                return DatabaseResponse.ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();

            return DatabaseResponse.ERROR;
        }
    }

    public DatabaseResponse updateSessionDate(Integer sessionID, LocalDate newStartDate, LocalDate newEndDate) {
        String query = "SELECT * FROM Sessions WHERE id != " + sessionID;
        ResultSet sessions = queryExecutor.executeQuery(query);

        Timestamp newStartDate_ts = Timestamp.valueOf(newStartDate + " 10:00:00");
        Timestamp newEndDate_ts = Timestamp.valueOf(newEndDate + " 18:00:00");

        try {
            while (sessions.next()) {
                if (checkIfSessionDateIsOccupied(sessions, newStartDate_ts, newEndDate_ts))
                    return DatabaseResponse.SESSION_DATE_OCCUPIED;
            }
            String updateSessionDateQuery = "UPDATE Sessions SET start_date = (?), end_date = (?) WHERE id = (?)";
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(updateSessionDateQuery);

                return getDatabaseResponse(newStartDate_ts, newEndDate_ts, sessionID, preparedStatement);
            } catch (SQLException e) {
                alertManager.throwError("Wystąpił błąd podczas zapisu danych do bazy");
            } catch (Exception e) {
                alertManager.throwError("Cos poszło nie tak. Sprawdź wprowadzone dane");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();

            return DatabaseResponse.ERROR;
        }

        return DatabaseResponse.ERROR;
    }

    private boolean checkIfSessionDateIsOccupied(ResultSet sessions, Timestamp newStartDate, Timestamp newEndDate) throws SQLException {
        Timestamp startDate = sessions.getTimestamp("start_date");
        Timestamp endDate = sessions.getTimestamp("end_date");

        return newStartDate.after(startDate) || newStartDate.before(endDate);
    }

    public DatabaseResponse updateEquipmentQuantity(Integer id, String newQuantity) {
        String query = "UPDATE Equipment SET quantity = (?) WHERE id = (?)";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);

            return getDatabaseResponse(newQuantity, id, preparedStatement);
        } catch (SQLException e) {
            alertManager.throwError("Wystąpił błąd podczas zapisu danych do bazy");
        } catch (Exception e) {
            alertManager.throwError("Cos poszło nie tak. Sprawdź wprowadzone dane.");
        }

        return DatabaseResponse.ERROR;
    }

    private DatabaseResponse getDatabaseResponse(String first_name, String last_name, String username, String password, String role, String email, String phone_number, PreparedStatement preparedClient) throws SQLException {
        preparedClient.setString(1, first_name);
        preparedClient.setString(2, last_name);
        preparedClient.setString(3, username);
        preparedClient.setString(4, password);
        preparedClient.setString(5, role);
        preparedClient.setString(6, email);
        preparedClient.setString(7, phone_number);
        preparedClient.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(String session_name, String band_name, Timestamp start_date, Timestamp end_date, Integer duration, Integer Clients_id, Integer Engineer_id, PreparedStatement preparedSession) throws SQLException {
        preparedSession.setString(1, session_name);
        preparedSession.setString(2, band_name);
        preparedSession.setTimestamp(3, start_date);
        preparedSession.setTimestamp(4, end_date);
        preparedSession.setInt(5, duration);
        preparedSession.setInt(6, Clients_id);
        preparedSession.setInt(7, Engineer_id);
        preparedSession.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(Timestamp newStartDate, Timestamp newEndDate, Integer sessionID, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setTimestamp(1, newStartDate);
        preparedStatement.setTimestamp(2, newEndDate);
        preparedStatement.setInt(3, sessionID);
        preparedStatement.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(String newQuantity, Integer id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, newQuantity);
        preparedStatement.setInt(2, id);
        preparedStatement.execute();

        return DatabaseResponse.SUCCESS;
    }

    public DatabaseResponse delete(Session session) {
        String deleteQuery = "DELETE FROM Sessions WHERE id = " + session.getId();

        return performDeletion(deleteQuery);
    }

    public DatabaseResponse delete(Equipment equipment) {
        String deleteQuery = "DELETE FROM Equipment WHERE id = " + equipment.getId();

        return performDeletion(deleteQuery);
    }
}
