package com.studionagranapp.helpers.errorhandling;

public interface IAlertManager {
    void throwError(String message);
    void throwConfirmation(String message);
}
