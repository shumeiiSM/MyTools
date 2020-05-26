package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CheckTools extends AppCompatActivity {

    ImageView ivBack;
    ListView lvTool;

    Boolean myAvail;
    Long myID;

    FirebaseFirestore db;

    private List<String> toolList = new ArrayList<>();
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_tools);

        ivBack = findViewById(R.id.ivBack);

        db = FirebaseFirestore.getInstance();
        lvTool = findViewById(R.id.lvTool);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intentNewAct);
            }
        });


        db.collection("Inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                toolList.clear();

                for (DocumentSnapshot snapshot : documentSnapshots) {
                    String myname = (String) snapshot.get("name");
                    myID = (Long) snapshot.get("id");
                    myAvail = (Boolean) snapshot.get("availability");

                    toolList.add(myname + " - " + myID);


                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, toolList);
                adapter.notifyDataSetChanged();
                lvTool.setAdapter(adapter);
            }
        });

        lvTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String name = adapterView.getItemAtPosition(position).toString();
                String[] selectedID = name.split(" - ");

                Intent intent = new Intent(getBaseContext(), ToolDetails.class);
                intent.putExtra("id", selectedID[1]);
                startActivity(intent);
            }
        });


    }
}
