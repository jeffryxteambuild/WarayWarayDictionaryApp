package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class toolbar extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        ImageButton back = findViewById(R.id.back_btn);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(toolbar.this, "Back Button", Toast.LENGTH_SHORT).show();
        //   startActivity(new Intent(getBaseContext(), HomeFragment.class));
    }

}