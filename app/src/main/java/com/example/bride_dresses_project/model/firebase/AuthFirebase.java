package com.example.bride_dresses_project.model.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class AuthFirebase {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public interface RegisterListener {
        void onComplete(String uid);
    }

    public void register(String email, String password, RegisterListener listener){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful() && task.getResult().getUser() != null){
                        listener.onComplete(task.getResult().getUser().getUid());
                    }
                });
    }

    public interface LoginOnSuccessListener {
        void onComplete();
    }

    public interface LoginOnFailureListener {
        void onComplete(String errorMessage);
    }

    public void login(String email, String password, LoginOnSuccessListener onSuccessListener, LoginOnFailureListener onFailureListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(t -> {
                    onSuccessListener.onComplete();
                })
                .addOnFailureListener(command -> {
                    onFailureListener.onComplete(command.getMessage());
                });
    }

    public interface LogoutListener {
        void onComplete();
    }

    public void logout(LogoutListener listener) {
        mAuth.signOut();
        listener.onComplete();
    }

    public boolean isUserConnected(){
        return (mAuth.getCurrentUser() != null);
    }

    public String getCurrentUserUid() {
        if (isUserConnected()) {
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public interface IsEmailExistOnSuccessListener {
        void onComplete(boolean emailExist);
    }

    public interface IsEmailExistOnFailureListener {
        void onComplete(String errorMessage);
    }

    public void isEmailExist(String email, IsEmailExistOnSuccessListener onSuccessListener, IsEmailExistOnFailureListener onFailureListener) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(task -> {
                    if(task.getSignInMethods() != null) {
                        boolean isNewUser = task.getSignInMethods().isEmpty();
                        onSuccessListener.onComplete(!isNewUser);
                    }
                })
                .addOnFailureListener(command -> {
                    onFailureListener.onComplete(command.getMessage());
                });
    }


//    public void register(String email, String password, Model.RegisterListener registerListener) {
//        Log.d("tag","auth register");
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("tag", "createUserWithEmail:success");
//                            //FirebaseUser user = mAuth.getCurrentUser();
//                            registerListener.onComplete(task.getResult().getUser().getUid());
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("tag", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(MyApplication.getContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//    }

//    public void login(String email, String password, Model.LoginListener loginListener) {
//        Log.d("tag","auth");
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("tag", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            loginListener.onComplete();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("tag", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(MyApplication.getContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//    }
//
//    public boolean isSignedIn(){
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        return (currentUser != null);
//    }

}
