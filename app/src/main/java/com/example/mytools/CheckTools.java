package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class CheckTools extends AppCompatActivity {

    ImageView ivBack, ivDate;
    ListView lvTool;

    SearchView svSearch;

    TextView tvDate;
    Spinner spAvail;
    Button btnSearch;

    String currentR, dateChosen;
    Boolean myAvail, currentAvail;
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
        ivDate = findViewById(R.id.ivDate);

        svSearch = findViewById(R.id.svSearch);

        tvDate = findViewById(R.id.tvDate);
        spAvail = findViewById(R.id.spinner);
        btnSearch = findViewById(R.id.btnSearch);


        db = FirebaseFirestore.getInstance();
        lvTool = findViewById(R.id.lvTool);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intentNewAct);
            }
        });

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                };

                // Create the DatePicker Dialog
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDateDialog = new DatePickerDialog(CheckTools.this,
                        myDateListener, mYear, mMonth, mDay);

                myDateDialog.show();
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setText("");
            }
        });

        searchData();
        /*
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
         */

        lvTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String name = adapterView.getItemAtPosition(position).toString();
                String[] selectedID = name.split(" - ");

                Intent intent = new Intent(getBaseContext(), ToolDetails.class);
                intent.putExtra("checkId", selectedID[1]);
                startActivity(intent);
            }
        });

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (s.isEmpty() == false) {

                    db.collection("Inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                            toolList.clear();

                            for (DocumentSnapshot snapshot : documentSnapshots) {
                                String myname = (String) snapshot.get("name");
                                myID = (Long) snapshot.get("id");
                                myAvail = (Boolean) snapshot.get("availability");

                                long numS = Long.parseLong(s);

                                if (s.equals(myname) || numS == myID) {
                                    toolList.add(myname + " - " + myID);
                                }


                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, toolList);
                            adapter.notifyDataSetChanged();
                            lvTool.setAdapter(adapter);
                        }
                    });

                    /*
                    if (toolList.size() > 1){
                        Toast.makeText(CheckTools.this, "Yes!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckTools.this, "No!", Toast.LENGTH_SHORT).show();
                    }
                     */


                } else {
                    searchData();
                }

                return false;

            }
        });



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChosen = tvDate.getText().toString();
                String availChosen = spAvail.getSelectedItem().toString();

                if (availChosen.equals("Available")) {
                    currentR = "Yes";
                } else if (availChosen.equals("Unavailable")){
                    currentR = "No";
                } else {
                    currentR = "Nothing";
                }

                if (currentR.equals("Yes")) {
                    currentAvail = true;
                } else if (currentR.equals("No")) {
                    currentAvail = false;
                }

                db.collection("Inventory").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        toolList.clear();

                        for (DocumentSnapshot snapshot : documentSnapshots) {
                            String myname = (String) snapshot.get("name");
                            String myDate = (String) snapshot.get("date");
                            myID = (Long) snapshot.get("id");
                            myAvail = (Boolean) snapshot.get("availability");

                            if (myDate.equals(dateChosen) || myAvail.equals(currentAvail)) {
                                toolList.add(myname + " - " + myID);
                            }

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1, toolList);
                        adapter.notifyDataSetChanged();
                        lvTool.setAdapter(adapter);
                    }
                });

                // IF BOTH CHOICES EMPTY
                if (dateChosen.isEmpty() && (currentR.equals("Nothing"))) {
                    searchData();
                } else if (!dateChosen.isEmpty() && (currentR.equals("Yes"))) {

                } else if (!dateChosen.isEmpty() && (currentR.equals("No"))) {

                }



            }
        });


    }

    private void searchData() {
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
    }
}


