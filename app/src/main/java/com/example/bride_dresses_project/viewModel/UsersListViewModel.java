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

    private LiveData<List<User>> usersList = Model.instance.getAllUsers();
    public LiveData<List<User>> getUsersList(){
        Log.d("tag1 ViewModel ",  usersList.toString() );
        return usersList;
    }

}
