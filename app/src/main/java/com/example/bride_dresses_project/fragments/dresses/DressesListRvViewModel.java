package com.example.bride_dresses_project.fragments.dresses;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.Dress;
import java.util.List;

public class DressesListRvViewModel {LiveData<List<Dress>> data;

    public DressesListRvViewModel(){
//        data = Model.instance.getAll();
    }
    public LiveData<List<Dress>> getData() {
        return data;
    }

}
