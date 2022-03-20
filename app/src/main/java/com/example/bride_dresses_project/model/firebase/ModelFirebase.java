package com.example.bride_dresses_project.model.firebase;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public static final String USER_IMAGE_FOLDER = "users";
    public static final String USER_COLLECTION_NAME = "users";
    public static final String DRESS_IMAGE_FOLDER = "Dresses";
    public static final String DRESS_COLLECTION_NAME = "Dresses";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();


    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    /* Authentication */
    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(AddUserListener listener, User user) {
        Map<String, Object> userJson = user.convertUserToJson();

        db.collection(USER_COLLECTION_NAME)
                .document(user.getId()).set(userJson)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public interface UploadUserImageListener {
        void onComplete(String url);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener) {
        final StorageReference imagesRef = storage.getReference().child(USER_IMAGE_FOLDER).child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    listener.onComplete(uri.toString());
                }));
    }




    /*********************************** Dresses*************************************/

    public interface GetAllDressesListener {
        void onComplete(List<Dress> list);
    }

    public void getAllDresses(Long lastUpdateDate, GetAllDressesListener listener) {
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Dress> list = new LinkedList<Dress>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Dress student = Dress.create(doc.getData());
                            if (student != null) {
                                list.add(student);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }


    public void addDress(Dress dress, Model.AddDressListener listener) {
        Map<String, Object> json = dress.toJson();
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .document(dress.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void getDressById(String studentId, Model.GetStudentById listener) {
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .document(studentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Dress student = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            student = Dress.create(task.getResult().getData());
                        }
                        listener.onComplete(student);
                    }
                });
    }


    /**
     * Firebase Storage
     */

    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("user_avatars/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
                });
    }


    public void updateDress(Dress r, Model.UpdateDressListener lis) {
        Map<String, Object> jsonReview = r.toMap();
        db.collection("Dresses")
                .document(r.getId())
                .update(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    /**
     * Authentication
     */
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }




}

