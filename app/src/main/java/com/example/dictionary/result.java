package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.dbdictionary.DbHelper;
import com.example.dictionary.utility.AudioHandler;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;


public class result extends AppCompatActivity {

    AutoCompleteTextView search;
    TextView word, fos, definition, translation, example, dialect, origin, contributor, other_words;
    DbHelper dbHelper;
    ImageButton save_btn;
    ImageButton voice_btn;
    MediaPlayer mediaPlayer;

    Spinner spinnerFilter;
    ArrayAdapter<String> farrayadapter;
    ArrayAdapter arrayAdapter;


    final String[] sorigin = {"All","Biliran", "Leyte", "Samar", "Northern Samar", "Eastern Samar", "Southern Leyte", "Ormoc", "Tacloban"};
    String filteredOrigin = "All";

    ArrayList<String>[] dataList;
    ArrayList<String> wordList;
    ArrayList<String> originList;

    boolean isSaved;

//    String text_type = HomeFragment.word_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        spinnerFilter = findViewById(R.id.filtersearch);
        farrayadapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sorigin);
        spinnerFilter.setAdapter(farrayadapter);

        save_btn = findViewById(R.id.save);
        voice_btn = findViewById(R.id.voice);

        save_event(save_btn);
        voice_event(voice_btn);


        word = findViewById(R.id.word);
        fos = findViewById(R.id.title_fos);
        definition = findViewById(R.id.content_definition);
        translation = findViewById(R.id.content_translation);
        example = findViewById(R.id.content_example);
        dialect = findViewById(R.id.content_dialect);
        origin = findViewById(R.id.content_origin);
        contributor = findViewById(R.id.content_contributor);
        other_words = findViewById(R.id.other_words);


        receiveData();
        display_wordContent(HomeFragment.content);

        ImageView back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataHome();
                result.this.finish();
            }
        });

        search = (AutoCompleteTextView) findViewById(R.id.search_view);

        dbHelper = new DbHelper(getApplicationContext(), "dictionary.db",1);
        try{
//            dbHelper.CheckDb();
            dbHelper.OpenDatabase();
        }catch (Exception e){}
//        dbHelper = MainActivity.getDbHelper();

        dataList = dbHelper.getWordList();
        wordList = new ArrayList<>(dataList[0]);
        originList = new ArrayList<>(dataList[1]);

        isSaved = dbHelper.saved_word(HomeFragment.word_selected);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
               display_wordContent(getAns(word));
               HomeFragment.word_selected = word;
                search.setText(null);

                dbHelper.history_word(word);

            }
        });

        int originIndex = 0;

        for(int index = 0; index < sorigin.length; index++) {
            if(filteredOrigin.equalsIgnoreCase(sorigin[index])) {
                originIndex = index;
                break;
            }

        }

        spinnerFilter.setSelection(originIndex);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hideKeyboard(view);
                filteredOrigin = sorigin[position];

                search.setHint(filteredOrigin);


                if (filteredOrigin.equalsIgnoreCase(sorigin[0])) {

                    wordList.clear();
                    originList.clear();
                    //arrayAdapter.notifyDataSetChanged();

                    for(int index = 0; index < dataList[0].size(); index++) {
                        String wOrigin = dataList[1].get(index);
                        String wWord = dataList[0].get(index);

                        wordList.add(wWord);
                        originList.add(wOrigin);

                        //arrayAdapter.notifyDataSetChanged();

                    }


                }  else  {

                    for(int index = 0; index < dataList[0].size(); index++) {
                        String wOrigin = dataList[1].get(index);
                        String wWord = dataList[0].get(index);

                        if (!filteredOrigin.equalsIgnoreCase(wOrigin)) {
                            originList.remove(wOrigin);
                            wordList.remove(wWord);
                        } else {
                            if (!wordList.contains(wWord)) {
                                wordList.add(wWord);
                            }
                            if (!originList.contains(wOrigin)) {
                                originList.add(wOrigin);
                            }
                        }

                        //arrayAdapter.notifyDataSetChanged();

                    }

                }

                arrayAdapter = new ArrayAdapter(result.this, android.R.layout.simple_list_item_1, wordList);
                arrayAdapter.setNotifyOnChange(true);
                search.setAdapter(arrayAdapter);
                search.setThreshold(0);


//            Toast.makeText(getContext(), filteredOrigin +wordList.size()+" "+originList.size(), Toast.LENGTH_LONG).show();
                arrayAdapter.notifyDataSetChanged();
//            switch (position) {
//                case 0:
//                    filteredOrigin =
//                    Toast.makeText(getContext(), "0", Toast.LENGTH_SHORT).show();
//                    break;
//                case 1:
//                    Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
//                    break;
//                case 3:
//                    Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
//                    break;
//                case 4:
//                    Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
//                    break;
//                case 5:
//                    Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
//                    break;
//                case 6:
//                    Toast.makeText(getContext(), "6", Toast.LENGTH_SHORT).show();
//                    break;
//                case 7:
//                    Toast.makeText(getContext(), "7", Toast.LENGTH_SHORT).show();
//                    break;
//                case 8:
//                    Toast.makeText(getContext(), "8", Toast.LENGTH_SHORT).show();
//
//            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void display_wordContent(Object[] wordContent) {

        word.setText((String)wordContent[1]);
        fos.setText((String)wordContent[2]);
        definition.setText((String)wordContent[3]);
        translation.setText((String)wordContent[4]);
        example.setText((String)wordContent[5]);
        contributor.setText((String)wordContent[6]);
        other_words.setText((String)wordContent[7]);
        if(wordContent[8] == null) {
            dialect.setText("No Entry");
        }
        else {
            dialect.setText((String)wordContent[8]);
        }
        if(wordContent[9] == null) {
            origin.setText("No Entry");
        }
        else {
            origin.setText((String)wordContent[9]);
        }


        try {
            if(HomeFragment.audio_seleted == null) {
                Toast.makeText(getApplicationContext(), "Audio not yet available ", Toast.LENGTH_LONG).show();
            }
            else {
                AudioHandler.saveToLocal(HomeFragment.audio_seleted, (String)wordContent[0], getApplicationContext());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SAVING FAILED: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }


    public Object[] getAns(String word) {
        Object[] content = dbHelper.getAns(word);
        String[] str_content = {
                (String)content[0],
                (String)content[1],
                (String)content[2],
                (String)content[3],
                (String)content[4],
                (String)content[5],
                (String)content[7],
                (String)content[8],
                (String)content[9],
                (String)content[10]


        };
        HomeFragment.content = str_content;
        HomeFragment.word_selected = word;
        HomeFragment.audio_seleted = (byte[])content[6];

        return str_content;
    }



    private void save_event(ImageButton save_btn) {

        save_btn.setImageResource(isSaved ? R.drawable.ic_bookmarks : R.drawable.ic_bookmarks_on);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = dbHelper.saved_word(HomeFragment.word_selected);
                if(isSaved) {
                    Snackbar.make(findViewById(R.id.save), "WORD SAVED", Snackbar.LENGTH_SHORT).show();


                }
                else {
                    Snackbar.make(findViewById(R.id.save), "WORD UNSAVED", Snackbar.LENGTH_SHORT).show();

                }

                save_btn.setImageResource(isSaved ? R.drawable.ic_bookmarks : R.drawable.ic_bookmarks_on);

            }
        });
    }

    private void voice_event(ImageButton voice_btn) {
        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mediaPlayer != null) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = AudioHandler.getmediaPlayer(getApplicationContext(), HomeFragment.word_selected);
                        }
                        else {
                            mediaPlayer.reset();
                        }

                    }
                    else {
                        mediaPlayer = AudioHandler.getmediaPlayer(getApplicationContext(), HomeFragment.word_selected);

                    }
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "you might not set the URI correctly", Toast.LENGTH_LONG).show();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();

                    }

                }

//                Toast.makeText(getApplicationContext(), "tingog ni thea", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void receiveData() {
        Intent intent = getIntent();
        HomeFragment.content = intent.getStringArrayExtra("content_key");
        HomeFragment.word_selected = intent.getStringExtra("word_key");
        HomeFragment.audio_seleted = intent.getByteArrayExtra("audio_key");
        filteredOrigin = intent.getStringExtra("filteredOrigin");

    }

    private void sendDataHome() {
        Intent intent = new Intent();
        intent.putExtra("filteredOrigin", filteredOrigin);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        sendDataHome();
    }

    /***
     *
     * hide keyboard
     */
    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}



