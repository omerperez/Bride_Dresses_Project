package com.example.bride_dresses_project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.model.room.AppLocalDb;
import com.google.type.DateTime;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    private final AuthFirebase authFirebase = new AuthFirebase();
    static ModelFirebase modelFirebase = new ModelFirebase();
    ModelSql modelSql = new ModelSql();
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum UserListLoadingState{
        loading,
        loaded
    }
    MutableLiveData<UserListLoadingState> userListLoadingState = new MutableLiveData<UserListLoadingState>();
    public LiveData<UserListLoadingState> getUserListLoadingState() {
        return userListLoadingState;
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    private Model(){
        userListLoadingState.setValue(UserListLoadingState.loaded);
    }

    public interface AddUserListener{
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener){
        modelFirebase.addUser(user, listener);
    }

    public interface GetUserById{
        void onComplete(User user);
    }


    MutableLiveData<List<User>> usersList;
    public LiveData<List<User>> getAll(){
        if (usersList.getValue() == null) {
            usersList=modelSql.getAllUsers();
            refreshUsersList();
        };
        return  usersList;
    }

    public interface GetAllUsersListener extends Listener<List<User>> {}

    public  MutableLiveData<List<User>> getAllUsers(){

        Long lastUpdateDate = new Long(0);
        modelFirebase.getAllUsers(lastUpdateDate, new GetAllUsersListener() {
            @Override
            public void onComplete(List<User> result) {
                usersList.setValue(result);
            }
        });
        return usersList;
    }


    public void refreshUsersList(){
        userListLoadingState.setValue(UserListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = User.getLocalLastUpdated();

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllUsers(lastUpdateDate, new GetAllUsersListener() {
            @Override
            public void onComplete(List<User> list) {
                // add all records to the local db
                long lastU = 0;
                for (User user: list) {
                    modelSql.addUser(user,null);
                    if(user.getUpdateDate()>lastUpdateDate){
                        lastU = user.getUpdateDate();
                    }
                }
                //update the local last update date
                User.setLocalLastUpdated(lastU);

                //returns the updates data to the listeners

                 List<User> stList = AppLocalDb.db.userDao().getAll();
                        usersList.postValue(stList);
                        userListLoadingState.postValue(UserListLoadingState.loaded);

                }
            });
    }

    /*
 public static void getAllUsers(final GetAllUsersListener listener){
        Log.d("tag1", "model here");

        Long lastUpdateDate = new Long(0);
        modelFirebase.getAllUsers(lastUpdateDate,listener);
    }

 */


/*


    public interface GetAllUsersListener extends Listener<List<User>> {}

    public void updateUser(final User designer, final AddUserListener listener){
        modelFirebase.updateuser(designer, listener);
    }

     */

    interface deleteListener extends AddUserListener{}
    public void deleteUser(User user, deleteListener listener){
        modelFirebase.deleteUser(user,listener);
    }

    public interface uploadImageListener extends Listener<String>{}

    public interface LoginListener extends AddUserListener{}

    public interface LogoutListener{
        void onComplete();
    }

    public interface RegisterListener extends Listener<String>{}

    public static void uploadImage(Bitmap imageBmp, String name,final uploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp,name,listener);
    }

    public void register(User user, String password,AddUserListener userLis) {
        Log.d("tag","model register");
        authFirebase.register(user.getEmail(), password, userId -> {
            Log.d("tag","auth");
            user.setId(userId);
            modelFirebase.addUser(user, userLis);
        });
    }

    public void login(String email, String password, final LoginListener listener) {
        Log.d("tag","model");
        authFirebase.login(email, password,listener);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
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

*/
    /* Dresses */

    public interface DressesListener<T> {
        void onComplete(T object);
        void onFailure(Exception e);
    }

    public interface AddDressListener extends DressesListener<FirebaseDressStatus>{
        @Override
        void onComplete(FirebaseDressStatus status);

        @Override
        void onFailure(Exception e);
    }


    public interface GetDressByIdListener extends DressesListener<Dress>{
        @Override
        void onComplete(Dress dress);

        @Override
        void onFailure(Exception e);
    }

    public interface GetAllDressesListener extends DressesListener<List<Dress>> {
        @Override
        void onComplete(List<Dress> dresses);

        @Override
        void onFailure(Exception e);
    }

    public interface UpdateDressListener extends DressesListener<FirebaseDressStatus> {
        @Override
        void onComplete(FirebaseDressStatus updateStatus);

        @Override
        void onFailure(Exception e);
    }

    public interface DeleteDressByIdListener extends DressesListener<FirebaseDressStatus> {
        @Override
        void onComplete(FirebaseDressStatus status);

        @Override
        void onFailure(Exception e);
    }

    public void getAllDresses(MutableLiveData<List<Dress>> dressListLiveData,MutableLiveData<Exception> exceptionLiveData){
        modelFirebase.getAllDresses(dressListLiveData,exceptionLiveData);
    }

    public void addDress(final Dress dress,Uri dressImageUri, final AddDressListener listener) {
        modelFirebase.addDress(dress,dressImageUri, listener);
    }
    public void getDressById(String dressId,final GetDressByIdListener listener){
        modelFirebase.getDressById(dressId,listener);
    }

    public void updateDress(final Dress dress, final UpdateDressListener listener) {
        modelFirebase.updateDress(dress, listener);
    }

    public void deleteDress(final String dressId, final DeleteDressByIdListener listener) {
        modelFirebase.deleteDress(dressId, listener);
    }


}

