package com.example.bride_dresses_project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Context;
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
import com.example.bride_dresses_project.ContextApplication;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;
import com.example.bride_dresses_project.model.firebase.AuthFirebase;
import com.example.bride_dresses_project.model.firebase.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

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
    public enum DressListLoadingState {
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
        dressListLoadingState.setValue(DressListLoadingState.loaded);
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


   /********************** Dresses *************************/

    MutableLiveData<DressListLoadingState> dressListLoadingState = new MutableLiveData<DressListLoadingState>();

    public LiveData<DressListLoadingState> getDressListLoadingState() {
        return dressListLoadingState;
    }

    MutableLiveData<List<Dress>> dressesList = new MutableLiveData<List<Dress>>();

    public LiveData<List<Dress>> getAll() {
        if (dressesList.getValue() == null) {
            refreshDressList();
        }
        ;
        return dressesList;
    }

    public void refreshDressList() {
        dressListLoadingState.setValue(DressListLoadingState.loading);
        Long lastUpdateDate = ContextApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("DressesLastUpdateDate", 0);
        executor.execute(() -> {
            List<Dress> stList = AppLocalDb.db.dressDao().getAll();
            dressesList.postValue(stList);
        });


        modelFirebase.getAllDresses(lastUpdateDate, new ModelFirebase.GetAllDressesListener() {
            @Override
            public void onComplete(List<Dress> list) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (Dress dress : list) {
                            AppLocalDb.db.dressDao().insertAll(dress);
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
                });
            }
        });
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

    /**
     * Authentication
     */
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public interface UpdateDressListener {
        void onComplete();
    }

    public void updateDress( Dress dress,  UpdateDressListener lis) {
        modelFirebase.updateDress(dress, lis);
    }

    public void deleteDress(Dress dress, UpdateDressListener lis) {
        dress.setDeleted(true);
        modelFirebase.updateDress(dress, lis);
    }

}

