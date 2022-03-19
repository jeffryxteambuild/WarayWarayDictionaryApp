package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.dictionary.dbdictionary.DbHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static DbHelper dbHelper;

    public static DbHelper getDbHelper(){
        return dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this, "dictionary.db",1);
        try{
            dbHelper.CheckDb();
            dbHelper.OpenDatabase();
        }catch (Exception e){}

       final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

       findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               drawerLayout.openDrawer(GravityCompat.START);
           }
       });

        NavigationView navigationView;
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}