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


        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllDresses(lastUpdateDate, new ModelFirebase.GetAllDressesListener() {
            @Override
            public void onComplete(List<Dress> list) {
                // add all records to the local db
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
                        // update last local update date
                        ContextApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("DressesLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
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


//    public interface GetAllDressesListener {
//            void onComplete(List<Dress> dresses);
//        }
//        public  void getAllDresses(final GetAllDressesListener listener)
//        {
//            modelFirebase.getAllDresses(listener);
//        }
//        public interface AddDressListener{
//            Void onComplete();
//        }
//        public void addDress(final Dress dress,final  AddDressListener listener){
//            modelFirebase.addDress(dress,listener);
//        }




//    /* Dresses */
//    public interface DressesListener<T> {
//        void onComplete(T object);
//        void onFailure(Exception e);
//    }
//
//    public interface AddDressListener extends DressesListener<FirebaseDressStatus>{
//        @Override
//        void onComplete(FirebaseDressStatus status);
//
//        @Override
//        void onFailure(Exception e);
//    }
//
//
//    public interface GetDressByIdListener extends DressesListener<Dress>{
//        @Override
//        void onComplete(Dress dress);
//
//        @Override
//        void onFailure(Exception e);
//    }
//
//    public interface GetAllDressesListener extends DressesListener<List<Dress>> {
//        @Override
//        void onComplete(List<Dress> dresses);
//
//        @Override
//        void onFailure(Exception e);
//    }
//
//public interface UpdateDressListener {
//    void onComplete();
//}
//
//    public interface DeleteDressByIdListener extends DressesListener<FirebaseDressStatus> {
//        @Override
//        void onComplete(FirebaseDressStatus status);
//
//        @Override
//        void onFailure(Exception e);
//    }
//
//    public void getAllDresses(MutableLiveData<List<Dress>> dressListLiveData,MutableLiveData<Exception> exceptionLiveData){
//        modelFirebase.getAllDresses(dressListLiveData,exceptionLiveData);
//    }
//
//    public void addDress(final Dress dress,Uri dressImageUri, final AddDressListener listener) {
//        modelFirebase.addDress(dress,dressImageUri, listener);
//    }
//    public void getDressById(String dressId,final GetDressByIdListener listener){
//        modelFirebase.getDressById(dressId,listener);
//    }
//
//    public void updateDress( Dress dress,  UpdateDressListener lis) {
//        modelFirebase.updateDress(dress, lis);
//    }
//
//    public void deleteDress(Dress dress, UpdateDressListener lis) {
//        dress.setDeleted(true);
//        modelFirebase.updateDress(dress, lis);
//    }
//

}

