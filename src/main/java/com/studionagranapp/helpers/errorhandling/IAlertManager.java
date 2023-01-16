package com.studionagranapp.helpers.errorhandling;

public interface IAlertManager {
    void throwError(String message);
    void throwInformation(String message);
    boolean throwConfirmation(String message);
}
