package com.example.actionplannerapplication.Model;

/**
 * Class to receive data from the Cloud Firestore
 */
public class ActionModel extends TaskId {
    private String task;
    private String due;
    private int status;

    public String getTask() {
        return task;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
