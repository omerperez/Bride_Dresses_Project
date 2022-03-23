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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();

    private final AuthFirebase authFirebase = new AuthFirebase();
    private static ModelFirebase modelFirebase = new ModelFirebase();
    private static ModelSql modelSql = new ModelSql();

    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

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

    public interface GetUserByIdListener extends Listener<User> {}

    /* Need to do a function of getUserById */
    MutableLiveData<List<User>> usersList = new MutableLiveData<List<User>>();

    public LiveData<List<User>> getAllUsers() {

        if (usersList.getValue() == null) {
            Log.d("tag1  ",  "null here" );
            //usersList.postValue(AppLocalDb.db.userDao().getAll()) ;
            refreshUsersList();
            Log.d("tag1  ",  "model here");
        }
        //Log.d("tag1  ",  "model here" +usersList.getValue().size());
        return usersList;
    }

    public interface GetAllUsersListener extends Listener<List<User>> {}

    public void refreshUsersList() {
        userListLoadingState.setValue(UserListLoadingState.loading);
        Long lastUpdateDate = User.getLocalLastUpdated();
        executor.execute(() -> {
            List<User> userList = AppLocalDb.db.userDao().getAll();
            usersList.postValue(userList);
        });
        modelFirebase.getAllUsers(lastUpdateDate, list -> executor.execute(new Runnable() {
            @Override
            public void run() {
                Long lastUpdate = new Long(0);
                for (User user : list) {
                    Log.d("tag", "return"+String.valueOf(list.size()));
                    if (lastUpdate < user.getUpdateDate()) {
                        lastUpdate = user.getUpdateDate();
                        AppLocalDb.db.userDao().insertAll(user);
                    }
                }
                User.setLocalLastUpdated(lastUpdate);
                List<User> userList = AppLocalDb.db.userDao().getAll();
                usersList.postValue(userList);
                userListLoadingState.postValue(UserListLoadingState.loaded);
            }
        }));
    }

    public interface uploadImageListener extends Listener<String> {}

    public interface LogoutListener {
        void onComplete();
    }

    public interface RegisterListener extends Listener<String> {}

    public static void uploadImage(Bitmap imageBmp, String name, final uploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public void register(User user, String password, AddUserListener userLis) {
        authFirebase.register(user.getEmail(), password, userId -> {
            user.setId(userId);
            modelFirebase.addUser(user, userLis);
        });
    }

    public interface LoginListener extends AddUserListener {}
    public void login(String email, String password, final LoginListener listener) {
        authFirebase.login(email, password, listener);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
    }

    public String getOwnerId() {
        if(isSignedIn()){
            return authFirebase.getCurrentUserid();
        }
        return null;
    }

    public void logout(final LogoutListener listener) {
        authFirebase.logout(listener);
    }

    public interface UserByIdListener {
        void onComplete(User user);
    }

    public void getUserById(String ownerId, UserByIdListener listener) {
        User user = AppLocalDb.db.userDao().getById(ownerId);
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

    String userId = authFirebase.getCurrentUserid();

    public void refreshDressList(Boolean isOwnerDressesList) {
        dressListLoadingState.postValue(DressListLoadingState.loading);
        Long lastUpdateDate = ContextApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("DressesLastUpdateDate", 0);
        executor.execute(() -> {
            List<Dress> stList = new ArrayList<Dress>();
            if(isOwnerDressesList){
                Log.d("tag1  ",  userId);
                stList = AppLocalDb.db.dressDao().getByUserId(userId);
            } else {
                stList = AppLocalDb.db.dressDao().getAll();
            }
            dressesList.postValue(stList);
        });

        modelFirebase.getAllDresses(lastUpdateDate, list -> executor.execute(new Runnable() {
            @Override
            public void run() {
                Long lud = new Long(0);
                Integer counter = 0;
                for (Dress dress : list) {
                    if (dress.isDeleted()) {
                        lud = dress.getUpdateDate();
                        AppLocalDb.db.dressDao().delete(dress);
                    } else {
                        if (lud < dress.getUpdateDate() && !dress.isDeleted()) {
                            lud = dress.getUpdateDate();
                            AppLocalDb.db.dressDao().insertAll(dress);
                        }
                    }
                }
                ContextApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("DressesLastUpdateDate", lud)
                        .commit();

                List<Dress> stList = new ArrayList<Dress>();
                if(isOwnerDressesList){
                    stList = AppLocalDb.db.dressDao().getByUserId(userId);
                } else {
                    stList = AppLocalDb.db.dressDao().getAll();
                }
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
            refreshDressList(false);
        });
    }

    public void editDress(Dress dress, AddDressListener listener) {
        modelFirebase.editDress(dress, () -> {
            listener.onComplete();
            refreshDressList(false);
        });
    }

    public interface GetDressById {
        void onComplete(Dress dress);
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

    public void deleteDress(Dress dress, UpdateDressListener lis) {
        dress.setDeleted(true);
        modelFirebase.updateDress(dress, lis);
    }

}

