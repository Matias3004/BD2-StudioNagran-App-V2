package com.studionagranapp.helpers.configurators.datepickerconfigurators;

import com.studionagranapp.helpers.databaseconnection.DatabaseManager;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatePickerConfigurator {

    public void provideConfiguration(String query, DatePicker datePicker) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            ResultSet sessions = databaseManager.executeQuery(query);

            List<LocalDate> occupiedDates = new ArrayList<>();
            while (sessions.next()) {
                LocalDate start = sessions.getTimestamp("start_date")
                        .toLocalDateTime().toLocalDate();
                LocalDate end = sessions.getTimestamp("end_date")
                        .toLocalDateTime().toLocalDate();
                while (!start.isAfter(end)) {
                    occupiedDates.add(start);
                    start = start.plusDays(1);
                }
            }

            datePicker.setDayCellFactory(new Callback<>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (!empty && item != null) {
                                if (occupiedDates.contains(item)) {
                                    this.setStyle("-fx-background-color: #DD0000");
                                    this.setDisable(true);
                                }
                            }
                        }
                    };
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
