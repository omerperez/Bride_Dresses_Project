package com.example.bride_dresses_project.fragments.dresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;

import java.util.List;

public class DressesListRvViewModel extends ViewModel {
    LiveData<List<Dress>> data;

    public DressesListRvViewModel(){
        data = Model.instance.getAll();
    }
    public LiveData<List<Dress>> getData() {
        return data;
    }
}
