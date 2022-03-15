package com.example.bride_dresses_project.model.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bride_dresses_project.model.Dress;
import com.example.bride_dresses_project.model.MyApplication;
import com.example.bride_dresses_project.model.User;
import com.example.bride_dresses_project.model.room.daos.DressDao;
import com.example.bride_dresses_project.model.room.daos.UserDao;

@Database(entities = {User.class, Dress.class}, version = 2)

abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract DressDao DressDao();

}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}


