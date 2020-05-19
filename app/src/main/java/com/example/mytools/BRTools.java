package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class BRTools extends AppCompatActivity {

    ImageView ivBack;
    // Button btnBorrow, btnReturn;

    TextView tv;
    FloatingActionButton fab;

    ListView lvTool;
    FirebaseFirestore db;

    Boolean myresult, myAresult;
    Boolean myAvail;

    Long myID;
    private List<String> toolList = new ArrayList<>();
    private CollectionReference colRef;
    private DocumentReference docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_br_tools);

        ivBack = findViewById(R.id.ivBack);
        tv = findViewById(R.id.tv);
        fab = findViewById(R.id.fab);

        db = FirebaseFirestore.getInstance();
        lvTool = findViewById(R.id.lvTool);

        // btnBorrow = findViewById(R.id.btnBorrow);
        // btnReturn = findViewById(R.id.btnReturn);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intentNewAct);
            }
        });

        /*
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), Borrow.class);
                startActivity(intentNewAct);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), Return.class);
                startActivity(intentNewAct);
            }
        });
         */

        db.collection("Inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                toolList.clear();

                for (DocumentSnapshot snapshot : documentSnapshots) {
                    String myname = (String) snapshot.get("name");
                    myID = (Long) snapshot.get("id");
                    myAvail = (Boolean) snapshot.get("availability");

                    if (!myAvail) {
                        toolList.add(myname + " - " + myID);
                    }


                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, toolList);
                adapter.notifyDataSetChanged();
                lvTool.setAdapter(adapter);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.input, null);

                final EditText etID = viewDialog.findViewById(R.id.etID);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(BRTools.this);
                myBuilder.setView(viewDialog);
                myBuilder.setTitle("Enter the Tool's ID");
                myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String theID = etID.getText().toString();
                        tv.setText(theID);


                        db.collection("Inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                                for (DocumentSnapshot snapshot : documentSnapshots) {
                                    myID = (Long) snapshot.get("id");
                                    myAvail = (Boolean) snapshot.get("availability");

                                    if (Long.valueOf(theID).equals(myID)) {
                                        colRef = db.collection("Inventory");
                                        docRef = colRef.document(theID);
                                        docRef.update("availability",false);
                                        myresult = true;

                                        // CHECK FOR EXISTING BORROW TOOL
//                                    } else if (Long.valueOf(theID).equals(myID) && !myAvail){
//                                        myAresult = true;

                                    }



                                }
                            }
                        });


                        if (myresult != null && myresult) {
                            Toast.makeText(BRTools.this, "You have borrowed ID: " + theID, Toast.LENGTH_SHORT).show();
//                        } else if  (myAresult != null && myAresult) {
//                            Toast.makeText(BRTools.this, "This tool ID: " + theID + " is on loaned.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(BRTools.this, "No such ID: " + theID, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myBuilder.setNegativeButton("CANCEL", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });


        lvTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String name = adapterView.getItemAtPosition(position).toString();
                String[] selectedID = name.split(" - ");

                Intent intent = new Intent(getBaseContext(), BorrowDetails.class);
                intent.putExtra("id", selectedID[1]);
                startActivity(intent);
            }
        });








    }
}
