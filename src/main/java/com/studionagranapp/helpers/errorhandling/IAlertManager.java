package com.studionagranapp.helpers.errorhandling;

import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface IAlertManager {
    void throwError(String message);
    void throwInformation(String message);
    boolean throwConfirmation(String message);
}
