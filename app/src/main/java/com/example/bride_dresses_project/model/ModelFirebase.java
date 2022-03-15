package com.example.bride_dresses_project.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
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
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://bridedressesproject-default-rtdb.firebaseio.com/");
    FirebaseStorage storage = FirebaseStorage.getInstance();
  DatabaseReference databaseReference = firebaseDatabase.getReference("designers");
  StorageReference storageReference;

  User designer;
    List<User> designersList = new ArrayList<>();

    public List<User> getDesignersList() {
        return designersList;
    }

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

    public void getDataFromFirebase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                designer = new User(map.get("fullName"),
                                        map.get("phone"),
                                        map.get("password"),
                                        map.get("streetAddress"),
                                        map.get("state"),
                                        map.get("country"));

                designersList.add(designer);
                Log.d("tag1", String.valueOf(designersList.size()));

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

    public void updateDesigner(User designer, Model.AddDesignerListener listener) {
        addDesigner(designer,listener);
    }

    public void getAllDesigners(Model.GetAllDesignersListener listener) {
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

    public void getDesiner(String phone, Model.GetDesignerListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Designers").document(phone).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User designer= null;
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc != null){
                        designer = task.getResult().toObject(User.class);
                    }
                }
                listener.onComplete(designer);
            }
        });
    }

    public void deleteDesigner(User designer, Model.deleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Designers").document(designer.getPhone()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete();
                    }
                });
    }

    public void addUser(User user, Model.AddUserListener listener) {
        Map<String, Object> json = user.toJson();
        db.collection("Users")
                .document(user.getPhone())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public interface GetAllDesignersListener{
        void onComplete(List<User> list);
    }

    public interface GetAllDressesListener{
        void onComplete(List<Dress> list);
    }

    /*
    public void createDesigner(User designer, Uri profileImage , Model.AddDesignerListener listener) {
        Map<String, Object> json = designer.toJson();
        StorageReference riversFile = storageReference.child("images/" + designer.getImage());
        riversFile.putFile(profileImage);
        db.collection("Designer")
                .document(designer.getPhone())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

     */

    public void addDesigner(User designer, Model.AddDesignerListener listener){
        db.collection("Designer")
                .document(designer.getPhone())
                .set(designer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("tag","designer added successfully");
                        listener.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tag","failed added designer");
                listener.onComplete();
            }
        });
    }

    public void getDesignerByPhone(String designerPhoneNumber, Model.GetDesignerById listener) {
        db.collection("Designer")
                .document(designerPhoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User designer = null;
                        if (task.isSuccessful() & task.getResult()!= null){
                            designer = User.create(task.getResult().getData());
                        }
                        listener.onComplete(designer);
                    }
                });

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

    public void getAllDresses(Model.GetAllDressesListener listener) {
        db.collection("Dresses") // name
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Dress> allDresses = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            allDresses.add (doc.toObject(Dress.class));
                        }
                        listener.onComplete(allDresses);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });

    }

}
