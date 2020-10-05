package com.example.journal_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journal_app.utils.JournalApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn, signUpBtn;
    private AutoCompleteTextView email, password;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
    }

    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.loginProgress);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpBtn:
                startActivity(new Intent(LoginActivity.this, CreateAccActivity.class));
                break;
            case R.id.signInBtn:
                login(email.getText().toString().trim(), password.getText().toString().trim());
                break;
        }
    }

    private void login(String emailString, String passwordString) {
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passwordString)){
            firebaseAuth.signInWithEmailAndPassword(emailString,passwordString)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String curUserID = user.getUid();
                            ref
                                    .whereEqualTo("userId", curUserID)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                              if (error != null){
                                                  return;
                                              }
                                            assert value != null;
                                            if (!value.isEmpty()){
                                                progressBar.setVisibility(View.INVISIBLE);

                                                for (QueryDocumentSnapshot snapshot : value){
                                                    JournalApi journalApi = JournalApi.getInstance();
                                                    journalApi.setUsername(snapshot.getString("username"));
                                                    journalApi.setUserId(snapshot.getString("userId"));
                                                }
                                                startActivity(new Intent(LoginActivity.this, JournalListActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Field can not be null", Toast.LENGTH_SHORT).show();
        }
    }
}