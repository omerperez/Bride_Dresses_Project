package com.example.bride_dresses_project.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.bride_dresses_project.model.entities.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE id = :userId")
    User getById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllDresses(User... users);

    @Delete
    void deleteDress(User user);

}
