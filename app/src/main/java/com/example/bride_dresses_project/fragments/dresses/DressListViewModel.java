package com.example.bride_dresses_project.fragments.dresses;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;

import java.util.List;

public class DressListViewModel extends ViewModel {

    private Model model;
    private MutableLiveData<List<Dress>> dressesLiveData = new MutableLiveData<>();
    private MutableLiveData<Exception> exceptionsLiveData = new MutableLiveData<>();
    public DressListViewModel() {
        model = Model.instance;
        model.getAllDresses(dressesLiveData,exceptionsLiveData);

    }

    public MutableLiveData<Exception> getExceptionsLiveData() {
        return exceptionsLiveData;
    }

    public MutableLiveData<List<Dress>> getDressesLiveData() {
        return dressesLiveData;
    }
}
