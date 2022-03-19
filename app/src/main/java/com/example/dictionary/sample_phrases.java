package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dictionary.utility.AudioHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sample_phrases extends AppCompatActivity {

    HashMap<String, List<String>> listChild;
    List<String> listHeader;
    ListAdapter listAdapter;
    MediaPlayer mediaPlayer;
    ImageButton voice2;
    Bundle bundle;
    String warayTranslationSelected;
    int position;
    boolean hasExpandAnother;
    String[] dialogues;
    String[] translation;


    private ExpandableListView mExpandableListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_phrases);


        ImageView back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sample_phrases.this.finish();

            }
        });

        voice2 = findViewById(R.id.voice2);


        mExpandableListView = findViewById(R.id.phrases);

        listData();


        listAdapter = new ListAdapter(this, listChild, listHeader);

        mExpandableListView.setAdapter(listAdapter);

//        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                return false;
//            }
//        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                String warayTranslationSelected = translation[groupPosition];

                String recentExpandPosition = translation[groupPosition];
                sample_phrases.this.warayTranslationSelected = recentExpandPosition;
//
//                hasExpandAnother = position != groupPosition;
//                if(warayTranslationSelected != null) {
//                    if (hasExpandAnother) {
//                        mExpandableListView.collapseGroup(position);
//                    }
//                }

                position = groupPosition;

                return false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int prevGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != prevGroup)
                    mExpandableListView.collapseGroup(prevGroup);
                prevGroup =  groupPosition;


            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

    }



    private void listData() {

        listHeader = new ArrayList<String>();
        listChild = new HashMap<String, List<String>>();

        Intent intent = getIntent();

        dialogues = intent.getStringArrayExtra("englishkey");
        translation = intent.getStringArrayExtra("waraykey");
        bundle = intent.getBundleExtra("audiokey");
        for(String dialogue : dialogues) {
            listHeader.add(dialogue);
        }
        for (int i = 0; translation.length > i; i++) {
            ArrayList<String> dialoguelist = new ArrayList<String>();
            dialoguelist.add(translation[i]);
            listChild.put(listHeader.get(i), dialoguelist);

        }

    }

    class ListAdapter extends BaseExpandableListAdapter {

        private final Context context;
        private final HashMap<String, List<String>> listChild;
        private final List<String> listHeader;

        public ListAdapter(Context context, HashMap<String, List<String>> listChild, List<String> listHeader) {
            this.context = context;
            this.listChild = listChild;
            this.listHeader = listHeader;

        }


        @Override
        public int getGroupCount() {
            return listHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listChild.get(listHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listChild.get(listHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            String headerTitle = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.group_item, null);

            }

            TextView textView = convertView.findViewById(R.id.eng_phrase);
            textView.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            String childTitle = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.child_item, null);

            }

            TextView textView = convertView.findViewById(R.id.waray_translation);
            textView.setText(childTitle);

            try {
                if(childTitle == null) {
                    Toast.makeText(getApplicationContext(), "Audio not yet available ", Toast.LENGTH_LONG).show();
                }
                else {
                    AudioHandler.saveToLocal(bundle.getByteArray(childTitle), childTitle, getApplicationContext());
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "SAVING FAILED: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }




    public void audioTranslate_btn(View view) {

        try {
            if (mediaPlayer != null) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = AudioHandler.getmediaPlayer(getApplicationContext(), sample_phrases.this.warayTranslationSelected);
                }
                else {
                    mediaPlayer.reset();
                }
            }
            else {
                mediaPlayer = AudioHandler.getmediaPlayer(getApplicationContext(), sample_phrases.this.warayTranslationSelected);

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

    }

}

