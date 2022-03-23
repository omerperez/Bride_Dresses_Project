package com.example.bride_dresses_project.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.User;

import java.util.LinkedList;
import java.util.List;

public class UsersListViewModel extends ViewModel {

    LiveData<List<User>> usersList;

    public UsersListViewModel(){
        usersList = Model.instance.getAllUsers();
    }


    public LiveData<List<User>> getUsersList(){
        Log.d("tag1 ViewModel ",  "usersList.getValue().toString()" );
        return usersList;
    }

}
