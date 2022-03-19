package com.example.bride_dresses_project.model.firebase;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    public static final String USER_IMAGE_FOLDER = "users";
    public static final String USER_COLLECTION_NAME = "users";
    public static final String DRESS_IMAGE_FOLDER = "dresses";
    public static final String DRESS_COLLECTION_NAME = "dresses";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();


    public ModelFirebase(){
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

    public void addUser(AddUserListener listener, User user){
        Map<String, Object> userJson = user.convertUserToJson();

        db.collection(USER_COLLECTION_NAME)
                .document(user.getId()).set(userJson)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public interface UploadUserImageListener {
        void onComplete(String url);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener){
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


//    public interface DeleteListener {
//        void onComplete();
//    }
//    public void deleteUser(User user, DeleteListener listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(USER_COLLECTION_NAME).document(user.getEmail()).delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        listener.onComplete();
//                    }
//                });
//    }

    /* Dresses*/
    public void getDressById(String dressId, Model.GetDressByIdListener listener) {
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
    public void updateDress (Dress r, Model.UpdateDressListener lis) {
        Map<String, Object> jsonReview = r.toMap();
        db.collection("Dresses")
                .document(r.getId())
                .update(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void addDress(Dress dress,Uri dressImageUri, Model.AddDressListener listener) {
        final StorageReference dressRef = storage.getReference().child(DRESS_COLLECTION_NAME).child(dress.getId());
        dressRef.putFile(dressImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if(taskSnapshot.getMetadata() != null) {
                        dressRef.getDownloadUrl().addOnSuccessListener(uri -> {
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
            // להוסיף מקרה שהוא לא הצליח להביא את השמלות
        });

    }

//    public interface GetAllDressesListener{
//        void onComplete(List<Dress> list);
//    }

}

//    public static void uploadImage(Bitmap imageBmp, String name, Model.uploadImageListener listener){
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        final StorageReference imagesRef = storage.getReference().child("images").child(name);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imagesRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception exception) {
//                listener.onComplete(null);
//                Log.d("tag","skdas");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    Uri downloadUrl = uri;
//                    Log.d("tag", String.valueOf(uri));
//                    listener.onComplete(downloadUrl.toString());
//                });
//            }
//        });
//    }

/*

public void getDataFromFirebase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                user = new User(map.get("email"),
                        map.get("fullName"),
                        map.get("phone"),
                        map.get("streetAddress"),
                        map.get("state"),
                        map.get("country"));

                usersList.add(user);
                Log.d("tag1", String.valueOf(usersList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

*/