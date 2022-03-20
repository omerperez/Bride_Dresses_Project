package com.example.bride_dresses_project.model;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.model.room.AppLocalDb;

import java.util.List;

public class ModelSql {


    public MutableLiveData<List<User>> getAllUsers(){
       return AppLocalDb.db.userDao().getAll();

    }
     /*
    public interface AddUserListener{
        void onComplete();
    }
     */

    //TODO- edit to livedata

    public void addUser(final User user,final Model.AddUserListener listener){
        class MyAsyncTask extends AsyncTask{
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.userDao().insertAll(user);
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener !=null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
