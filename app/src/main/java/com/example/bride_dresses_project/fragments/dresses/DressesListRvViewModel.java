package com.example.bride_dresses_project.fragments.dresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.firebase.ModelFirebase;

import java.util.List;

public class DressesListRvViewModel extends ViewModel {


    public LiveData<List<Dress>> getAllDresses() {
        return Model.instance.getAllDresses();
    }

    public void refreshDressList(Boolean isOnlyOwnerDresses) {
        Model.instance.refreshDressList(isOnlyOwnerDresses);
    }
}
