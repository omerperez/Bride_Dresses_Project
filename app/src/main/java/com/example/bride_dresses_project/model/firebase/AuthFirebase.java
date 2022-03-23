package com.example.bride_dresses_project.model.firebase;

import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.bride_dresses_project.ContextApplication;
import com.example.bride_dresses_project.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void register(String email, String password, Model.RegisterListener registerListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerListener.onComplete(task.getResult().getUser().getUid());
                        } else {
                            Toast.makeText(ContextApplication.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(String email, String password, Model.LoginListener loginListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginListener.onComplete();
                        } else {
                            Toast.makeText(ContextApplication.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public String getCurrentUserid() {
        if(isSignedIn()){
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public void logout(Model.LogoutListener lis) {
        mAuth.signOut();
        lis.onComplete();
    }
}
