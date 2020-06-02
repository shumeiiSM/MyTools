package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class ToolDetails extends AppCompatActivity {

    ImageView iv, ivBack, ivAvail;
    TextView tvName, tvID, tvDate, tvAvail;

    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_details);

        iv = findViewById(R.id.iv);
        ivBack = findViewById(R.id.ivBack);
        ivAvail = findViewById(R.id.ivAvail);

        tvName = findViewById(R.id.tvName);
        tvID = findViewById(R.id.tvID);
        tvDate = findViewById(R.id.tvDate);
        tvAvail = findViewById(R.id.tvAvail);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        final String myid = i.getStringExtra("checkId");

        db = FirebaseFirestore.getInstance();

        colRef = db.collection("Inventory");
        docRef = colRef.document(myid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String name = (String) snapshot.get("name");
                    Long id = (Long) snapshot.get("id");
                    String date = (String) snapshot.get("date");
                    String image = (String) snapshot.get("imageURL");
                    Boolean avail = (Boolean) snapshot.get("availability");


                    tvName.setText(name);
                    tvDate.setText(date);
                    tvID.setText(String.valueOf(id));

                    if (avail != null & avail) {
                        Picasso.with(getBaseContext()).load(R.drawable.tick).error(R.drawable.ic_yes).resize(50, 50).into(ivAvail);
                        tvAvail.setText("Available");
                    } else {
                        Picasso.with(getBaseContext()).load(R.drawable.cross).error(R.drawable.ic_no).resize(50,50).into(ivAvail);
                        tvAvail.setText("Unavailable");
                    }

                    Picasso.with(getBaseContext()).load(image).resize(800,500).into(iv);
                }
            }
        });


    }
}
