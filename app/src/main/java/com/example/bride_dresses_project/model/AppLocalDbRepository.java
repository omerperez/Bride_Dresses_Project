package com.example.bride_dresses_project.model;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bride_dresses_project.model.dao.DressDao;
import com.example.bride_dresses_project.model.dao.UserDao;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;

@Database(entities = {User.class, Dress.class}, version = 13)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DressDao dressDao();
}