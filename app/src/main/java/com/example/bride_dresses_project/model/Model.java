package com.example.bride_dresses_project.model;

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

    MutableLiveData<DesignerListLoadingState> designerListLoadingState = new MutableLiveData<DesignerListLoadingState>();
    public LiveData<DesignerListLoadingState> getDesignerListLoadingState() {
        return designerListLoadingState;
    }

    static ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){
        designerListLoadingState.setValue(DesignerListLoadingState.loaded);
    }


    public interface AddDesignerListener{
        void onComplete();
    }

    public void createDesigner(Designer designer, Uri profileImage, AddDesignerListener listener){
        modelFirebase.createDesigner( designer, profileImage ,listener);
    }

    public interface GetDesignerById{
        void onComplete(Designer designer);
    }

    public Designer getDesignerByPhone(String designerPhoneNumber, GetDesignerById listener) {
        modelFirebase.getDesignerByPhone(designerPhoneNumber, listener);
        return null;
    }

    public static void getDataFromFirebase()
    {
        modelFirebase.getDataFromFirebase();
    }

    public interface GetDesignerListener{
        void onComplete(Designer designer);
    }

    public void GetDesigner(String phone, GetDesignerListener listener) {
        modelFirebase.getDesiner(phone,listener);
    }


    public interface GetAllDesignersListener{
        void onComplete(List<Designer> data);
    }


    public void getAllDesigners(final GetAllDesignersListener listener){
        modelFirebase.getAllDesigners(listener);
    }

    public interface UpdateDesignerListener extends AddDesignerListener {

    }

    public void updateDesigner(final Designer designer, final AddDesignerListener listener){
        modelFirebase.updateDesigner(designer, listener);
    }

    interface deleteListener extends AddDesignerListener{

    }
   /* public void deleteDesigner(Designer designer, deleteListener listener){
        modelFirebase.deleteDesigner(designer,listener);
    }*/

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
