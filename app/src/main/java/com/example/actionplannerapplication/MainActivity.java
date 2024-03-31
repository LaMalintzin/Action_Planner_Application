package com.example.actionplannerapplication;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.actionplannerapplication.Adapter.ActionAdapter;
import com.example.actionplannerapplication.Model.ActionModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private MyAdapter adapter;
    private FloatingActionButton mFaButton;
    private FirebaseFirestore firestore;
    private ActionAdapter adapter;
    //private List<String> dataList;
    private List<ActionModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        mFaButton = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // Initialize your dataList here
        //dataList = new ArrayList<>();
        // Add some sample data to the list
        //dataList.add("Task 1");
        //dataList.add("Task 2");
        //dataList.add("Task 3");

        //adapter = new MyAdapter(dataList);
        //recyclerView.setAdapter(adapter);

        mFaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        dataList = new ArrayList<>();
        adapter = new ActionAdapter(dataList, MainActivity.this);

        recyclerView.setAdapter(adapter);
        showData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Method keeping track and displaying changes from
     * the firestore database through an activity listener.
     */
    private void showData(){
        firestore.collection("task").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ActionModel actionModel = documentChange.getDocument().toObject(ActionModel.class).withID(id);

                        dataList.add(actionModel);
                        adapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(dataList);
            }
        });
    }
}