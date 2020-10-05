package com.example.journal_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journal_app.utils.JournalApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccActivity extends AppCompatActivity {
    private Button signUpBtn;
    private AutoCompleteTextView email, password;
    private EditText username;
    private ProgressBar progressBar;

//    firestore connection
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        initViews();
    }

    private void initViews() {
//        Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null){
                    // Logged IN
                }else {

                }
            }
        };

        username = findViewById(R.id.username_acc);
        email = findViewById(R.id.email_acc);
        password = findViewById(R.id.password_acc);
        signUpBtn = findViewById(R.id.signInBtn_acc);
        progressBar = findViewById(R.id.createAccProgress);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserByEmail(email.getText().toString().trim(), password.getText().toString().trim(), username.getText().toString().trim());
            }
        });
    }

    private void createUserByEmail(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String curUserId = currentUser.getUid();

                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", curUserId);
                                userObj.put("username", username);

                                // Save to firestore
                                ref.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (Objects.requireNonNull(task.getResult()).exists()){
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    String name = task.getResult().getString("username");

                                                    JournalApi journalApi = JournalApi.getInstance();
                                                    journalApi.setUserId(curUserId);
                                                    journalApi.setUsername(name);

                                                    Intent intent = new Intent(CreateAccActivity.this, JournalListActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }else {

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateAccActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this, "Field can not be null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}