package com.example.bride_dresses_project.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.bride_dresses_project.model.entities.Dress;

import java.util.List;

@Dao
public interface DressDao {

    @Query("select * from Dress")
    List<Dress> getAll();

    @Query("SELECT * FROM Dress WHERE id=:dressId")
    Dress getById(String dressId);

    @Transaction
    @Query("SELECT * FROM Dress WHERE id=:userId")
    List<Dress> getByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Dress... dresses);

    @Delete
    void delete(Dress dress);

}