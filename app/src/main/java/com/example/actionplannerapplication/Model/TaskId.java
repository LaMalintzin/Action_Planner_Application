package com.example.actionplannerapplication.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TaskId {

    // @Exclude excludes TaskId from being manipulated when changing other variables
    @Exclude
    public String TaskId;

    public <T extends TaskId> T withID(@NonNull final String id){
        this.TaskId = id;
        return (T) this;
    }
}
