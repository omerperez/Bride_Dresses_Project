package com.example.bride_dresses_project.model.SQL;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.model.AppLocalDb;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.User;

import java.util.List;

public class ModelSql {

    public LiveData<List<User>> getAllUsers(){
        return AppLocalDb.db.userDao().getAll();
    }

    public void addUser(final User user,final Model.AddUserListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.userDao().insertAll(user);
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
