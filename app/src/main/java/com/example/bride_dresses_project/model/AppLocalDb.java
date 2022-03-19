package com.example.bride_dresses_project.model;

import androidx.room.Room;

import com.example.bride_dresses_project.ContextApplication;

public class AppLocalDb {

    static public AppLocalDbRepository db =
            Room.databaseBuilder(ContextApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();

}
