package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class BorrowDetails extends AppCompatActivity {

    ImageView iv, ivBack;
    TextView tvName, tvID, tvDate;

    Button btnReturn;

    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_details);

        iv = findViewById(R.id.iv);
        ivBack = findViewById(R.id.ivBack);

        tvName = findViewById(R.id.tvName);
        tvID = findViewById(R.id.tvID);
        tvDate = findViewById(R.id.tvDate);

        btnReturn = findViewById(R.id.btnReturn);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), BRTools.class);
                startActivity(intentNewAct);
            }
        });


        Intent i = getIntent();
        final String myid = i.getStringExtra("id");

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


                    tvName.setText(name);
                    tvDate.setText(date);
                    tvID.setText(String.valueOf(id));
                    Picasso.with(getBaseContext()).load(image).resize(800,500).into(iv);
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(BorrowDetails.this);
                myBuilder.setTitle("Confirmation");
                myBuilder.setMessage("Are you sure to return this tool?");
                myBuilder.setCancelable(false);

                myBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        colRef = db.collection("Inventory");
                        docRef = colRef.document(myid);
                        docRef.update("availability",true);

                        Toast.makeText(BorrowDetails.this, "You have successfully return tool ID: " + myid, Toast.LENGTH_LONG).show();

                        Intent intentNewAct = new Intent(getBaseContext(), BRTools.class);
                        startActivity(intentNewAct);
                    }
                });

                myBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BorrowDetails.this, "NO was selected", Toast.LENGTH_SHORT).show();
                    }
                });

                myBuilder.setNeutralButton("CANCEL", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });






    }
}
