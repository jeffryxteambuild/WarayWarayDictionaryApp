package com.example.dictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dictionary.dbdictionary.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class SavedFragment extends Fragment {

    ArrayAdapter<String> arrayAdapter;

    DbHelper dbHelper;
    ListView listView;
    FloatingActionButton clear_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_saved, container, false);

        clear_btn = v.findViewById(R.id.ClearSaved_btn);

        listView = v.findViewById(R.id.saved_listview);

        dbHelper = new DbHelper(getContext(), "dictionary.db",1);
        try{
            dbHelper.CheckDb();
            dbHelper.OpenDatabase();
        }catch (Exception e){}

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getSavedlist(dbHelper));
        listView.setAdapter(arrayAdapter);
        listview_event();
        clearsaved_event();

        return v;
    }

    private ArrayList<String> getSavedlist(DbHelper dbHelper) {
        ArrayList<String> reverse_list = dbHelper.getSavedList();
        Collections.reverse(reverse_list);
        return reverse_list;
    }

    private void listview_event() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
                HomeFragment.word_selected = word;
                HomeFragment.content = dbHelper.getAns(word);
                Intent intent = new Intent(getContext(),result.class);
                Object[] content = HomeFragment.content;
                String[] str_content = {
                        (String)content[0],
                        (String)content[1],
                        (String)content[2],
                        (String)content[3],
                        (String)content[4],
                        (String)content[5],
                        (String)content[7],
                        (String)content[8]
                };
                intent.putExtra("content_key", str_content);
                intent.putExtra("word_key", HomeFragment.word_selected);
                intent.putExtra("audio_key", (byte[])content[6]);
                startActivity(intent);
                getActivity().onBackPressed();
            }
        });

    }

    private void clearsaved_event() {

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setTitle("Confirm Clear Saved");

                alertDialogBuilder.setMessage("Are you sure to clear saved list?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.clearsaved();

                                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
                                listView.setAdapter(arrayAdapter);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

    }

}