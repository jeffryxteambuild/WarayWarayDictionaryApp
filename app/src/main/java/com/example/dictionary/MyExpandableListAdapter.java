package com.example.dictionary;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<String>> samplephrases;
    private List<String> phraselist;

    public MyExpandableListAdapter(Context context, List<String> phraselist, Map<String, List<String>> samplephrases) {
        this.context = context;
        this.samplephrases = samplephrases;
        this.phraselist = phraselist;

    }

    @Override
    public int getGroupCount() {
        return samplephrases.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return samplephrases.get(phraselist.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return phraselist.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return samplephrases.get(phraselist.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String phrase_eng = phraselist.get(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView item = view.findViewById(R.id.eng_phrase);
        item.setTypeface(null, Typeface.BOLD);;
        item.setText(phrase_eng);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String phrase_waray = getChild(i, i1).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_item, null);
        }
        TextView item = view.findViewById(R.id.waray_translation);
        ImageView speaker = view.findViewById(R.id.voice);
        item.setText(phrase_waray);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
