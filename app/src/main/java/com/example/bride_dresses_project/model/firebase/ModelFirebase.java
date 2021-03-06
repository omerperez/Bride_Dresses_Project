package com.example.bride_dresses_project.model.firebase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.bride_dresses_project.model.AppLocalDb;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    public static final String USER_IMAGE_FOLDER = "images";
    public static final String USER_COLLECTION_NAME = "Users";
    public static final String DRESS_IMAGE_FOLDER = "Dresses";
    public static final String DRESS_COLLECTION_NAME = "Dresses";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    // storageRef

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    /*********************************** Users *************************************/

    public void getAllUsers(Long lastUpdateDate, Model.GetAllUsersListener listener) {
        db.collection(USER_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<User>();
                    if (task.isSuccessful()) {
                        Log.d("tag", "firebase check");
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Log.d("tag", "firebaseee 0 "+task.getResult().toString());
                            User user = User.create(doc.getData());
                            Log.d("tag", "firebaseee 1 "+user.getPhone());
                            list.add(user);
                        }
                    }
                    Log.d("tag", "firebaseee 2 " +list.size());

                    listener.onComplete(list);
                });
    }

    public void getUserById(String id, Model.GetUserByIdListener listener) {
        db.collection(USER_COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = null;
                        if (task.isSuccessful() && task.getResult() != null) {
                            user = User.create(task.getResult().getData());
                        }
                        listener.onComplete(user);
                    }
                });
    }


    public void addUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection(USER_COLLECTION_NAME)
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }


    public void uploadImage(Bitmap imageBmp, String name, Model.uploadImageListener listener) {
        final StorageReference imagesRef = storage.getReference().child(USER_IMAGE_FOLDER).child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null)).addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
            listener.onComplete(uri.toString());
        }));
    }

    /*********************************** Dresses *************************************/

    public interface GetAllDressesListener {
        void onComplete(List<Dress> list);
    }

    public void getAllDresses(Long lastUpdateDate, GetAllDressesListener listener) {
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Dress> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Dress d = Dress.create(doc.getData());
                            if (d != null) {
                                list.add(d);
                            }
                        }
                    }
Log.d("tag", "dress"+String.valueOf(list.size())) ;
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

    public void getDressById(String dressId, Model.GetDressById listener) {
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .document(dressId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Dress d = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            d = Dress.create(task.getResult().getData());
                        }
                        listener.onComplete(d);
                    }
                });
    }

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

    public void updateDress(Dress d, Model.UpdateDressListener lis) {
        Map<String, Object> jsonReview = d.toMap();
        db.collection("Dresses")
                .document(d.getId())
                .update(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void editDress(Dress dress, Model.AddDressListener listener) {
        db.collection(Dress.DRESS_COLLECTION_NAME)
                .document(dress.getId())
                .update(dress.toEditJson()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new Thread(() -> AppLocalDb.db.dressDao().update(dress.getType(), dress.getPrice(), dress.getImageUrl(), dress.getId())).start();
                listener.onComplete();
            }
        });
    }

}

