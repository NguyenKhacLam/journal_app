package com.example.journal_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.journal_app.adapters.JournalAdapter;
import com.example.journal_app.modals.Journal;
import com.example.journal_app.utils.JournalApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class JournalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JournalAdapter journalAdapter;
    private ArrayList<Journal> journalsList;

    private TextView noJournal;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference ref = db.collection("journals");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        initViews();
    }

    private void initViews() {
        journalsList = new ArrayList<>();

        noJournal = findViewById(R.id.tv_noThought);
        recyclerView = findViewById(R.id.rc_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if (user != null && firebaseAuth != null){
                    startActivity(new Intent(JournalListActivity.this, PostJournalActivity.class));
//                    finish();
                }
                break;
            case R.id.action_signOut:
                if (user != null && firebaseAuth != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(JournalListActivity.this, MainActivity.class));
//                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ref.whereEqualTo("userId", JournalApi.getInstance().getUserId()).get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                if (!snapshots.isEmpty()){
                    for (QueryDocumentSnapshot journals : snapshots){
                        Journal journal = journals.toObject(Journal.class);
                        journalsList.add(journal);
                    }

                    journalAdapter = new JournalAdapter(JournalListActivity.this, journalsList);
                    recyclerView.setAdapter(journalAdapter);
                    journalAdapter.notifyDataSetChanged();
                }else {
                    noJournal.setVisibility(View.VISIBLE);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}