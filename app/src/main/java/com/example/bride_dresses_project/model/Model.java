package com.example.bride_dresses_project.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.ContextApplication;
import com.example.bride_dresses_project.model.SQL.ModelSql;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;
import com.example.bride_dresses_project.model.firebase.AuthFirebase;
import com.example.bride_dresses_project.model.firebase.ModelFirebase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();

    private final AuthFirebase authFirebase = new AuthFirebase();
    private static ModelFirebase modelFirebase = new ModelFirebase();
    private static ModelSql modelSql = new ModelSql();

    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum UserListLoadingState {
        loading,
        loaded
    }

    public enum DressListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<UserListLoadingState> userListLoadingState = new MutableLiveData<UserListLoadingState>();

    public LiveData<UserListLoadingState> getUserListLoadingState() {
        return userListLoadingState;
    }

    private Model() {
        userListLoadingState.setValue(UserListLoadingState.loaded);
        dressListLoadingState.setValue(DressListLoadingState.loaded);
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener) {
        modelFirebase.addUser(user, listener);
    }

    public interface GetUserByIdListener extends Listener<User> {
    }

    /* Need to do a function of getUserById */

    LiveData<List<User>> usersList;

    public LiveData<List<User>> getAllUsers() {

        if (usersList == null) {
            usersList = modelSql.getAllUsers();
            refreshUsersList();
        }
        return usersList;
    }

    public interface GetAllUsersListener extends Listener<List<User>> {
    }

    public void refreshUsersList() {

        userListLoadingState.setValue(UserListLoadingState.loading);

        modelFirebase.getAllUsers(User.getLocalLastUpdated(), new GetAllUsersListener() {
            @Override
            public void onComplete(List<User> list) {
                long lastUpdate = 0;
                for (User user : list) {

                    if (user.getUpdateDate() > User.getLocalLastUpdated()) {
                        lastUpdate = user.getUpdateDate();
                        Log.d("Phone", user.getPhone());
                        modelSql.addUser(user, null);
//                        Log.d("tag", modelSql.getAllUsers().getValue().get(0).toString());
                    }
                }
                if (lastUpdate != 0) {
                    User.setLocalLastUpdated(lastUpdate);
                    usersList = AppLocalDb.db.userDao().getAll();
                }
                userListLoadingState.postValue(UserListLoadingState.loaded);
            }
        });
    }

//    public interface DeleteListener{
//        void onComplete();
//    }
//
//    public void deleteUser(User user, DeleteListener listener){
//        modelFirebase.deleteUser(user,listener);
//    }

    public interface uploadImageListener extends Listener<String> {
    }

    public interface LoginListener extends AddUserListener {
    }

    public interface LogoutListener {
        void onComplete();
    }

    public interface RegisterListener extends Listener<String> {
    }

    public static void uploadImage(Bitmap imageBmp, String name, final uploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public void register(User user, String password, AddUserListener userLis) {
        authFirebase.register(user.getEmail(), password, userId -> {
            user.setId(userId);
            modelFirebase.addUser(user, userLis);
        });
    }

    public void login(String email, String password, final LoginListener listener) {
        authFirebase.login(email, password, listener);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
    }

    public void logout(final LogoutListener listener) {
        authFirebase.logout(listener);
    }

  /*  public final static Model instance = new Model();

    ModelFirebase modelFirebase = new ModelFirebase();
    ModelSql modelSql = new ModelSql();

    public void getAllDresses(final GetAllDressListener listener){
        modelFirebase.getAllDresses(listener);
    }

    public void addDress(final Dress dress, final AddDressListener listener){
        modelFirebase.addDress(dress,listener);
    }


   /********************** Dresses *************************/

    MutableLiveData<DressListLoadingState> dressListLoadingState = new MutableLiveData<DressListLoadingState>();

    public LiveData<DressListLoadingState> getDressListLoadingState() {
        return dressListLoadingState;
    }

    MutableLiveData<List<Dress>> dressesList = new MutableLiveData<>();

    public LiveData<List<Dress>> getAllDresses() {
        return dressesList;
    }

    public void refreshDressList() {
        dressListLoadingState.postValue(DressListLoadingState.loading);
        Long lastUpdateDate = ContextApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("DressesLastUpdateDate", 0);
        executor.execute(() -> {
            List<Dress> stList = AppLocalDb.db.dressDao().getAll();
            dressesList.postValue(stList);
        });

        modelFirebase.getAllDresses(lastUpdateDate, list -> executor.execute(new Runnable() {
            @Override
            public void run() {
                Long lud = new Long(0);
                Log.d("TAG", "fb returned " + list.size());
                for (Dress dress : list) {
                    if(dress.isDeleted()) {
                        AppLocalDb.db.dressDao().delete(dress);
                    }
                    if (lud < dress.getUpdateDate()) {
                        lud = dress.getUpdateDate();
                    }
                }
                ContextApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("DressesLastUpdateDate", lud)
                        .commit();

                List<Dress> stList = AppLocalDb.db.dressDao().getAll();
                dressesList.postValue(stList);
                dressListLoadingState.postValue(DressListLoadingState.loaded);
            }
        }));
    }

    public interface AddDressListener {
        void onComplete();
    }

    public void addDress(Dress dress, AddDressListener listener) {
        modelFirebase.addDress(dress, () -> {
            listener.onComplete();
            refreshDressList();
        });
    }

    public interface GetStudentById {
        void onComplete(Dress dress);
    }

    public Dress getStudentById(String studentId, GetStudentById listener) {
        modelFirebase.getDressById(studentId, listener);
        return null;
    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);

    }

    public interface UpdateDressListener {
        void onComplete();
    }

    public void updateDress(Dress dress, UpdateDressListener lis) {
        modelFirebase.updateDress(dress, lis);
    }

    public void deleteDress(Dress dress, UpdateDressListener lis) {
        dress.setDeleted(true);
        modelFirebase.updateDress(dress, lis);
    }

}

