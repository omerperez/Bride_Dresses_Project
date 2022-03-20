package com.example.bride_dresses_project.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.User;

import java.util.LinkedList;
import java.util.List;

public class UsersListViewModel extends ViewModel {

    private MutableLiveData<List<User>> usersList = Model.instance.getAllUsers();

    public MutableLiveData<List<User>> getUsersList(){
        return usersList;
    }

}
