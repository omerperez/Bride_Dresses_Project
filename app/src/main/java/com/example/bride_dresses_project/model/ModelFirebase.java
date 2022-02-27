package com.example.bride_dresses_project.model;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class ModelFirebase {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
//    DatabaseReference databaseReference;
    StorageReference storageReference;
//    private FirebaseDatabase database;
//    private DatabaseReference databaseReference;
//    private FirebaseStorage storage;
//    private StorageReference storageReference;

    public ModelFirebase(){
//        this.databaseReference = db.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");
        this.storageReference = storage.getReferenceFromUrl("gs://bridedressesproject.appspot.com");
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public interface GetAllDesignersListener{
        void onComplete(List<Designer> list);
    }

    public interface GetAllDressesListener{
        void onComplete(List<Dress> list);
    }

    public void createDesigner(Designer designer, Uri profileImage ,Model.AddDesignerListener listener) {
        Map<String, Object> json = designer.toJson();
        StorageReference riversFile = storageReference.child("images/" + designer.getImage());
        riversFile.putFile(profileImage);
        db.collection("Designer")
                .document(designer.getPhone())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getDesignerByPhone(String designerPhoneNumber, Model.GetDesignerById listener) {
        db.collection("Designer")
                .document(designerPhoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Designer designer = null;
                        if (task.isSuccessful() & task.getResult()!= null){
                            designer = Designer.create(task.getResult().getData());
                        }
                        listener.onComplete(designer);
                    }
                });

    }

    /*
    public void getAllDresses(Model.GetAllDressListener listener) {
        listener.onComplete(null);
    }

    public void addDress(Dress dress, Model.AddDressListener listener) {
    }
    */

}
