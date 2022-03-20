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
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public enum DesignerListLoadingState{
        loading,
        loaded
    }
    public enum DressListLoadingState {
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
        dressListLoadingState.setValue(DressListLoadingState.loaded);

    }

    public void register(ModelFirebase.AddUserListener addUserListener, User user, String password) {
        authFirebase.register(user.getEmail(), password, uid -> {
            user.setId(uid);
            modelFirebase.addUser(addUserListener, user);
        });
    }

    public static void uploadImage(Bitmap imageBmp, String name, ModelFirebase.UploadUserImageListener listener) {
        modelFirebase.uploadUserImage(imageBmp,name,listener);
    }

    public void login(String email, String password, AuthFirebase.LoginOnSuccessListener onSuccessListener, AuthFirebase.LoginOnFailureListener onFailureListener) {
        authFirebase.login(email, password, onSuccessListener, onFailureListener);
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

