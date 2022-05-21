package com.example.dictionary;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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
    ArrayList<String> wordList;
    ImageButton save_btn;
    ImageButton voice_btn;
    MediaPlayer mediaPlayer;

    boolean isSaved;

//    String text_type = HomeFragment.word_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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

        wordList = new ArrayList<>();
        wordList.addAll(dbHelper.getWordList());
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_list_item_1,wordList);
        search.setAdapter(arrayAdapter);
        search.setThreshold(0);

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

    }

}



