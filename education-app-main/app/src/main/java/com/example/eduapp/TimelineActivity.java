package com.example.eduapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TimelineActivity extends AppCompatActivity {

    private Set<String> savedCategories;
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final ArrayList<Post> list = new ArrayList<>();
    private TimeLineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        savedCategories = getSavedCategories();

        ListView listView = (ListView)findViewById(R.id.listView1);

        adapter = new TimeLineAdapter(TimelineActivity.this);
        adapter.setPostList(list);
        adapter.setOnKnownCategoryButtonClickListener(this::saveCategory);
        listView.setAdapter(adapter);

        getTopics();

        Button refresh = findViewById(R.id.button1);
        refresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTopics();
                    }
                }
        );

        Button newPost = findViewById(R.id.button2);
        newPost.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TimelineActivity.this, PostMainActivity.class);
                        startActivity(intent);
                    }

                }
        );
    }

    private void getTopics() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("topics")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .whereNotIn("category", new ArrayList<>(savedCategories))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = new Post();
                                post.setDate(df.format(document.getDate("created_at")).toString());
                                post.setBody(document.getString("body"));
                                post.setCategory(document.getString("category"));
                                list.add(post);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void saveCategory(String category) {
        savedCategories.add(category);
        SharedPreferences dataStore = getSharedPreferences("saved_categories", MODE_PRIVATE);
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putStringSet("categories", savedCategories);
        editor.apply();
    }
    private Set<String> getSavedCategories() {
        SharedPreferences dataStore = getSharedPreferences("saved_categories", MODE_PRIVATE);
        return dataStore.getStringSet("categories", new HashSet<>());
    }
}