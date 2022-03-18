package com.example.bride_dresses_project.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    private final AuthFirebase authFirebase = new AuthFirebase();
    static ModelFirebase modelFirebase = new ModelFirebase();
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum DesignerListLoadingState{
        loading,
        loaded
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    MutableLiveData<DesignerListLoadingState> designerListLoadingState = new MutableLiveData<DesignerListLoadingState>();
    public LiveData<DesignerListLoadingState> getDesignerListLoadingState() {
        return designerListLoadingState;
    }

    private Model(){
        designerListLoadingState.setValue(DesignerListLoadingState.loaded);
    }

    public interface AddUserListener{
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener){
        modelFirebase.addUser(user, listener);
    }

/*
    public void createDesigner(User designer, Uri profileImage, AddDesignerListener listener){
        modelFirebase.createDesigner( designer, profileImage ,listener);
    }
 */
    public interface GetUserById{
        void onComplete(User user);
    }
/*
    public User getUserById(String userId, GetUserById listener) {
        modelFirebase.getUserById(userId, listener);
        return null;
    }

 */

    public static void getDataFromFirebase()
    {
        modelFirebase.getDataFromFirebase();
    }

    public interface GetUserListener{
        void onComplete(User user);
    }
/*
    public void GetUser(String id, GetUserListener listener) {
        modelFirebase.getUserById(id,listener);
    }

    public interface GetAllUsersListener extends Listener<List<User>> {}


    public void getAllDesigners(final GetAllUsersListener listener){
        modelFirebase.getAllUsers(listener);
    }

    public void updateUser(final User designer, final AddUserListener listener){
        modelFirebase.updateuser(designer, listener);
    }

     */

    interface deleteListener extends AddUserListener{}
    public void deleteUser(User user, deleteListener listener){
        modelFirebase.deleteUser(user,listener);
    }

    public interface uploadImageListener extends Listener<String>{}

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

    public void login(String email, String password) {
        authFirebase.login(email, password);
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

