package com.example.bride_dresses_project.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
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

    static ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){
        designerListLoadingState.setValue(DesignerListLoadingState.loaded);
    }


    public interface AddUserListener{
        void onComplete();
    }

    public void addUser(User user, AddUserListener listener){
        modelFirebase.addUser(user, listener);
    }


    public interface AddDesignerListener{
        void onComplete();
    }
/*
    public void createDesigner(User designer, Uri profileImage, AddDesignerListener listener){
        modelFirebase.createDesigner( designer, profileImage ,listener);
    }


 */
    public interface GetDesignerById{
        void onComplete(User designer);
    }

    public User getDesignerByPhone(String designerPhoneNumber, GetDesignerById listener) {
        modelFirebase.getDesignerByPhone(designerPhoneNumber, listener);
        return null;
    }

    public static void getDataFromFirebase()
    {
        modelFirebase.getDataFromFirebase();
    }

    public interface GetDesignerListener{
        void onComplete(User designer);
    }

    public void GetDesigner(String phone, GetDesignerListener listener) {
        modelFirebase.getDesiner(phone,listener);
    }


    public interface GetAllDesignersListener extends Listener<List<User>> {}


    public void getAllDesigners(final GetAllDesignersListener listener){
        modelFirebase.getAllDesigners(listener);
    }

    public interface UpdateDesignerListener extends AddDesignerListener {

    }

    public void updateDesigner(final User designer, final AddDesignerListener listener){
        modelFirebase.updateDesigner(designer, listener);
    }

    interface deleteListener extends AddDesignerListener{}
    public void deleteDesigner(User designer, deleteListener listener){
        modelFirebase.deleteDesigner(designer,listener);
    }

    public interface uploadImageListener extends Listener<String>{}

    public static void uploadImage(Bitmap imageBmp, String name,final uploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp,name,listener);
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

    public void getAllDresses(final GetAllDressesListener listener){
        modelFirebase.getAllDresses(listener);
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

