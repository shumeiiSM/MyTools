package com.example.mytools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // HELLO YONG YI
    // BYEBYE
    // OK BYEBYE
    // LAST

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
                Intent intentNewAct = new Intent(getBaseContext(), AllTools.class);
                startActivity(intentNewAct);
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
