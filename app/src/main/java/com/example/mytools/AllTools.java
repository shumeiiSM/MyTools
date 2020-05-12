package com.example.mytools;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllTools extends Fragment {

    ImageView ivBack;

    // NAME FOR RETRIEVING DATABASE DATA
    String date, imageURL, name;
    int id;
    Boolean availability;

    private RecyclerView mRecyclerView;
    private List<AllToolDisplay> toolList;

    private FirebaseFirestore db;
    private CollectionReference colRef;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_tools, container, false);

        ivBack = view.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getContext(), MainActivity.class);
                startActivity(intentNewAct);
            }
        });

        toolList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();
        colRef = db.collection("Inventory");

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for(QueryDocumentSnapshot documentSnapshot : snapshot ) {
                    Inventory inventory = documentSnapshot.toObject(Inventory.class);

                    id = inventory.getId();
                    name = inventory.getName();
                    imageURL = inventory.getImageURL();
                    date = inventory.getDate();
                    availability = inventory.getAvailability();

                    toolList.add(new AllToolDisplay(id, name, imageURL, date, availability));


                /*
                    if(statuss != null && statuss.equalsIgnoreCase("pending") && currentUser.equals(namee)){
                        orderList.add(new SPOrder(id, namee, companyImagee, servicee, propertyy, datee, timee, unitt, pricee, userr, addresss, mobilee));

                    } else {
                        //Toast.makeText(getActivity(), "Not add", Toast.LENGTH_LONG).show();
                    }

                }
                */


                }

                AllToolDisplay_Adapter recyclerAdapter = new AllToolDisplay_Adapter(getContext(), toolList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(recyclerAdapter);

            }
        });







        return view;
    }
}
