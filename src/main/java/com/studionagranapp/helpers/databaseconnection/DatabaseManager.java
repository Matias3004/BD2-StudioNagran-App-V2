package com.studionagranapp.helpers.databaseconnection;

import com.studionagranapp.helpers.errorhandling.AlertManager;
import com.studionagranapp.helpers.models.Equipment;
import com.studionagranapp.helpers.models.Mix;
import com.studionagranapp.helpers.models.Session;
import com.studionagranapp.helpers.models.User;
import com.studionagranapp.helpers.query.QueryExecutor;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private final DatabaseConnector databaseConnector;
    private final QueryExecutor queryExecutor;
    private final AlertManager alertManager;

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
        String getSessionsQuery = "SELECT * FROM Sessions";
        ResultSet sessions = queryExecutor.executeQuery(getSessionsQuery);

        String engineerFirstName = engineer.split(" ")[0];
        String engineerLastName = engineer.split(" ")[1];

        String getEngineerIdQuery = "SELECT account_id FROM user_accounts " +
                "WHERE first_name = '" + engineerFirstName +
                "' AND last_name = '" + engineerLastName + "'";
        ResultSet engineerID = queryExecutor.executeQuery(getEngineerIdQuery);

        Integer duration = 8 * (int) ChronoUnit.DAYS.between(startDate, endDate);
        Timestamp start_date = Timestamp.valueOf(startDate + " 12:00:00");
        Timestamp end_date = Timestamp.valueOf(endDate + " 12:00:00");

        try {
            while (sessions.next()) {
                if (checkIfSessionDateIsOccupied(sessions, start_date, end_date))
                    return DatabaseResponse.SESSION_DATE_OCCUPIED;
            }

            int Engineer_id = 0;
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

        Timestamp newStartDate_ts = Timestamp.valueOf(newStartDate + " 12:00:00");
        Timestamp newEndDate_ts = Timestamp.valueOf(newEndDate + " 12:00:00");

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

        if (newStartDate.after(startDate) && newStartDate.before(endDate))
            return true;
        if (newEndDate.after(startDate) && newEndDate.before(endDate))
            return true;
        if (newStartDate.before(startDate) && newEndDate.after(endDate))
            return true;
        if (newStartDate.equals(startDate) || newEndDate.equals(endDate))
            return true;
        return newStartDate.equals(endDate) || newEndDate.equals(startDate);
    }

    public DatabaseResponse insertMix(String filename, String path, String sessionName) {
        String getSessionIdQuery = "SELECT id FROM Sessions WHERE session_name = '" + sessionName + "'";
        ResultSet sessionID = queryExecutor.executeQuery(getSessionIdQuery);

        try {
            int session_id = 0;
            if (sessionID.next())
                session_id = sessionID.getInt("id");

            String addMixQuery = "INSERT INTO Mixes (filename, upload_date, path, Session_id) VALUES (?,?,?,?)";
            try {
                PreparedStatement preparedMix = getConnection().prepareStatement(addMixQuery);
                preparedMix.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                return getDatabaseResponse(filename, path, session_id, preparedMix);
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

    public DatabaseResponse insertMixNote(String description, Integer mixID) {
        String query = "INSERT INTO Mix_notes (upload_date, description, Mix_id) VALUES (?,?,?)";
        try {
            PreparedStatement preparedMixNote = getConnection().prepareStatement(query);
            preparedMixNote.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            return getDatabaseResponse(description, mixID, preparedMixNote);
        } catch (SQLException e) {
            alertManager.throwError("Wystąpił błąd podczas zapisu danych do bazy");

            return DatabaseResponse.ERROR;
        } catch (Exception e) {
            alertManager.throwError("Cos poszło nie tak. Sprawdź wprowadzone dane");

            return DatabaseResponse.ERROR;
        }
    }

    public DatabaseResponse insertEquipment(String eqName, String eqType, String eqQuantity, String backline) {
        int isBackline = 0;
        if (backline.equals("Tak"))
            isBackline = 1;

        String query = "INSERT INTO Equipment (name, type, quantity, backline) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);

            return getDatabaseResponse(eqName, eqType, eqQuantity, isBackline, preparedStatement);
        } catch (SQLException e) {
            alertManager.throwError("Wystąpił błąd podczas zapisu danych do bazy");

            return DatabaseResponse.ERROR;
        } catch (Exception e) {
            alertManager.throwError("Cos poszło nie tak. Sprawdź wprowadzone dane");

            return DatabaseResponse.ERROR;
        }
    }

    public DatabaseResponse updateEquipmentQuantity(Integer id, String quantity) {
        int newQuantity = Integer.parseInt(quantity);

        String query = "UPDATE Equipment SET quantity = (?) WHERE id = (?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);

            return getDatabaseResponse(newQuantity, id, preparedStatement);
        } catch (SQLException e) {
            alertManager.throwError("Wystąpił błąd podczas zapisu danych do bazy");

            return DatabaseResponse.ERROR;
        } catch (Exception e) {
            alertManager.throwError("Cos poszło nie tak. Sprawdź wprowadzone dane.");

            return DatabaseResponse.ERROR;
        }
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

    private DatabaseResponse getDatabaseResponse(String filename, String path, Integer session_id, PreparedStatement preparedMix) throws SQLException {
        preparedMix.setString(1, filename);
        preparedMix.setString(3, path);
        preparedMix.setInt(4, session_id);
        preparedMix.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(String description, Integer mixID, PreparedStatement preparedMixNote) throws SQLException {
        preparedMixNote.setString(2, description);
        preparedMixNote.setInt(3, mixID);
        preparedMixNote.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(String eqName, String eqType, String eqQuantity, Integer backline, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, eqName);
        preparedStatement.setString(2, eqType);
        preparedStatement.setString(3, eqQuantity);
        preparedStatement.setInt(4, backline);
        preparedStatement.execute();

        return DatabaseResponse.SUCCESS;
    }

    private DatabaseResponse getDatabaseResponse(Integer newQuantity, Integer id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, newQuantity);
        preparedStatement.setInt(2, id);
        preparedStatement.execute();

        return DatabaseResponse.SUCCESS;
    }

    public DatabaseResponse delete(Session session) {
        String deleteQuery = "DELETE FROM Sessions WHERE id = " + session.getId();

        return performDeletion(deleteQuery);
    }

    public DatabaseResponse delete(User engineer) {
        String deleteQuery = "DELETE FROM User_accounts WHERE account_id = " + engineer.getAccountId();

        return performDeletion(deleteQuery);
    }

    public DatabaseResponse delete(Mix mix) {
        String deleteQuery = "DELETE FROM Mixes WHERE id = " + mix.getId();

        return performDeletion(deleteQuery);
    }

    public DatabaseResponse delete(Equipment equipment) {
        String deleteQuery = "DELETE FROM Equipment WHERE id = " + equipment.getId();

        return performDeletion(deleteQuery);
    }
}
