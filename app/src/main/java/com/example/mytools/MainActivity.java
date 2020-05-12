package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnBR, btnAll, btnCheck, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBR = findViewById(R.id.btnBR);
        btnAll = findViewById(R.id.btnAll);
        btnCheck = findViewById(R.id.btnCheck);
        btnAdd = findViewById(R.id.btnAdd);

        btnBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), BRTools.class);
                startActivity(intentNewAct);
            }
        });


        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllTools();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

                /*
                Fragment fragment = new AllTools();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                 */

                /*
                Intent intentNewAct = new Intent(getBaseContext(), AllTools.class);
                startActivity(intentNewAct);
                 */
            }
        });


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), CheckTools.class);
                startActivity(intentNewAct);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), AddTools.class);
                startActivity(intentNewAct);
            }
        });

    }
}
