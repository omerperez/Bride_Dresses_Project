package com.example.bride_dresses_project.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://bridedressesproject-default-rtdb.firebaseio.com/");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;


    public ModelFirebase(){
        storageReference = storage.getReference();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getAllUsers(Long lastUpdateDate, Model.GetAllUsersListener listener) {

            db.collection("Users")
                    .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                    .get()
                    .addOnCompleteListener(task -> {
                        List<User> list = new ArrayList<>();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                User student = User.create(doc.getData());
                                if (student != null){
                                    list.add(student);
                                }
                            }
                        }
                        listener.onComplete(list);
                    });

    }


    /*
    public void updateUser(User user, Model.AddUserListener listener) {
        addUser(user,listener);
    }

    public void getAllDesigners(Model.GetAllUsersListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Designers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> data = new LinkedList<User>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult()){
                        User designer = doc.toObject(User.class); //בונה מהפיירבייס אוביקט של מעצבת
                        data.add(designer);
                    }
                }
                listener.onComplete(data);
            }
        });
    }

    public void getUserById(String id, Model.GetUserListener listener) {
        db.collection("Users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user= null;
                if(task.isSuccessful()& task.getResult()!= null){
                    user = User.create(task.getResult().getData());
                    //DocumentSnapshot doc = task.getResult();
                    //if(doc != null){
                     //   user = task.getResult().toObject(User.class);
                    //}
                }
                listener.onComplete(user);
            }
        });
    }

     */

    public void deleteUser(User designer, Model.deleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(designer.getPhone()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete();
                    }
                });
    }

    public void addUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        Log.d("tag","add user firebase");
        db.collection("Users")
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public static void uploadImage(Bitmap imageBmp, String name, Model.uploadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
                Log.d("tag","skdas");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUrl = uri;
                    Log.d("tag", String.valueOf(uri));
                    listener.onComplete(downloadUrl.toString());
                });
            }
        });
    }




    /* Dresses*/

    public void getDressById(String dressId,Model.GetDressByIdListener listener) {
        db.collection("Dresses")
                .document(dressId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        listener.onComplete(documentSnapshot.toObject(Dress.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }


    public void updateDress(Dress dress,Model.UpdateDressListener listener) {

        db.collection("Dresses")
                .document(dress.getId())
                .set(dress)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete(new FirebaseDressStatus("Successfully updated " + dress.getId()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });

    }

    public void deleteDress(String dressId,Model.DeleteDressByIdListener listener) {
        db.collection("Dresses")
                .document(dressId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete(new FirebaseDressStatus("Successfully deleted dress " + dressId));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }


    public void addDress(Dress dress,Uri dressImageUri, Model.AddDressListener listener) {
        StorageReference dressStorageRef = storageReference.child("dresses/" + dress.getId());
        dressStorageRef.putFile(dressImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if(taskSnapshot.getMetadata() != null) {
                        dressStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            dress.setImageUrl(uri.toString());
                            db.collection("Dresses")
                                    .document(dress.getId())
                                    .set(dress)
                                    .addOnSuccessListener(unused -> listener.onComplete(new FirebaseDressStatus("Successfully added dress " + dress.getId()))).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listener.onFailure(e);
                                }
                            });
                        }).addOnFailureListener(listener::onFailure);

                    }
                }).addOnFailureListener(listener::onFailure);
    }

    public void getAllDresses(MutableLiveData<List<Dress>> dressListLiveData,MutableLiveData<Exception> exceptionLiveData) {
        db.collection("Dresses") // name
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Dress> allDresses = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            allDresses.add (doc.toObject(Dress.class));
                        }
                        dressListLiveData.postValue(allDresses);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exceptionLiveData.postValue(e);
            }
        });

    }

}
