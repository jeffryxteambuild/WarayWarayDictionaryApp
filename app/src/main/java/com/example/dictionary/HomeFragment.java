package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dictionary.dbdictionary.DbHelper;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {


    Button button_converse;
    Button button_greetings;
    Button button_directions;
    Button button_food;
    Button button_time;
    Button button_family;
    Button button_numbers;
    Button button_colors;
    Button button_emergency;
    Button button_sick;

    AutoCompleteTextView search;

    public static String word_selected;
    public static byte[] audio_seleted;

    DbHelper dbHelper;
    ArrayList<String> wordList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View v= inflater.inflate(R.layout.fragment_home, container, false);

        search = (AutoCompleteTextView) v.findViewById(R.id.search_view);


        dbHelper = new DbHelper(getContext(), "dictionary.db",1);
        try{
            dbHelper.CheckDb();
            dbHelper.OpenDatabase();
        }catch (Exception e){}

        wordList = new ArrayList<>();
        wordList.addAll(dbHelper.getWordList());
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(HomeFragment.this.getContext(), android.R.layout.simple_list_item_1,wordList);
        search.setAdapter(arrayAdapter);
        search.setThreshold(0);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                HomeFragment.word_selected = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
                word_selected = word;
                getAns(word);
                display_wordContent(word);
                search.setText(null);
                dbHelper.history_word(word);
            }
        });

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Object[] content = getAns(HomeFragment.word_selected);
                    if (content.length == 1) {
                        Toast.makeText(getContext(), "Word not found", Toast.LENGTH_LONG).show();
                    }
//                    else {
//                        display_wordContent();
//                        search.setText(null);
//                        dbHelper.history_word(word_selected);
//                    }
                }
                return false;
            }
        });


    button_converse= v.findViewById(R.id.button_converse);
    button_converse.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(),sample_phrases.class);
            ArrayList<Object[]> dialoguelist = dbHelper.getDialoguelist();
            String[] dialoguer = new String[dialoguelist.size()];
            String[] dialogue_waray = new String[dialoguelist.size()];
            byte[][] audiofiles = new byte[dialoguelist.size()][];
            Bundle bun = new Bundle();

            for(int i = 0; i < dialoguer.length; i++) {
                dialoguer[i] = (String)dialoguelist.get(i) [0];
                dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                bun.putByteArray(dialogue_waray[i], audiofiles[i]);

            }
            intent.putExtra("englishkey", dialoguer);
            intent.putExtra("waraykey", dialogue_waray);
            intent.putExtra("audiokey", bun);

            startActivity(intent);
        }
    });

        button_greetings= v.findViewById(R.id.button_greetings);
        button_greetings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getGreetinglist();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });

        button_directions = v.findViewById(R.id.button_directions);
        button_directions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getDirectionsList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });

        button_food= v.findViewById(R.id.button_food);
        button_food.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getFoodList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });

        button_time= v.findViewById(R.id.button_time);
        button_time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getTimeList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });

        button_family= v.findViewById(R.id.button_family);
        button_family.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getFamilyList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });


        button_numbers= v.findViewById(R.id.button_numbers);
        button_numbers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getNumbersList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });


        button_colors= v.findViewById(R.id.button_colors);
        button_colors.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getColorsList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });


        button_emergency= v.findViewById(R.id.button_emergency);
        button_emergency.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getEmergencyList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });


        button_sick= v.findViewById(R.id.button_sick);
        button_sick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> dialoguelist = dbHelper.getSickList();
                String[] dialoguer = new String[dialoguelist.size()];
                String[] dialogue_waray = new String[dialoguelist.size()];
                byte[][] audiofiles = new byte[dialoguelist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)dialoguelist.get(i) [0];
                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }
        });


        return v;
    }

    private void display_wordContent(String word) {
        Intent intent = new Intent(getContext(),result.class);
        Object[] content = getAns(word);
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
    }

    public static Object[] content = new Object[1];

    public Object[] getAns(String word) {
        content = dbHelper.getAns(word);

        return content;
    }

}