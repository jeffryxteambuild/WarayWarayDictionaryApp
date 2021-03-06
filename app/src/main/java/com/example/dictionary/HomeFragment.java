package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.dbdictionary.DbHelper;
import com.example.dictionary.spinner.PhrasesCategory;
import com.example.dictionary.spinner.data;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.dictionary.spinner.data.getPhraseList;


public class HomeFragment extends Fragment implements CustomSpinner.OnSpinnerEventsListener {

    int[] factImages = {R.drawable.wordart, R.drawable.map, R.drawable.bridge};
    String[] factTexts = {"Waray (also known as Waray-Waray) is an Austronesian language and the fifth-most-spoken native regional language of the Philippines, native to Eastern Visayas. It is the native language of the Waray people and second language of the Abaknon people of Capul, Northern Samar, and some Cebuano-speaking peoples of western and southern parts of Leyte island. It is the third most spoken language among the Bisayan languages, only behind Cebuano and Hiligaynon.",
            "Eastern Visayas, is an administrative region in the Philippines, designated as Region VIII. It consists of three main islands, Samar, Leyte and Biliran. The region has six provinces, one independent city and one highly urbanized city namely, Biliran, Leyte, Northern Samar, Samar, Eastern Samar, Southern Leyte, Ormoc and Tacloban. The highly urbanized city of Tacloban is the sole regional center. These provinces and cities occupy the easternmost islands of the Visayas group of islands.",
    "The San Juanico Bridge is part of the Pan-Philippine Highway and stretches from Samar to Leyte across the San Juanico Strait in the Philippines."};

    Spinner spinnerFilter;

    ArrayAdapter<String> farrayadapter;
    ArrayAdapter arrayAdapter;

    private AdapterViewFlipper mAdapterViewFlipper;

    private CustomSpinner spinner_category;

    private CategoryAdapter adapter;

    AutoCompleteTextView search;

    public static String word_selected;
    public static byte[] audio_seleted;

    DbHelper dbHelper;
    ArrayList<String>[] dataList;
    ArrayList<String> wordList;
    ArrayList<String> originList;

    public static final int REQUEST_CODE_1 = 500;

    final String[] origin = {"All","Biliran", "Leyte", "Samar", "Northern Samar", "Eastern Samar", "Southern Leyte", "Ormoc", "Tacloban"};
    String filteredOrigin = "All";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

        search = (AutoCompleteTextView) v.findViewById(R.id.search_view);

        spinnerFilter = v.findViewById(R.id.filtersearch);
        farrayadapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, origin);
        spinnerFilter.setAdapter(farrayadapter);

        spinner_category = v.findViewById(R.id.spinner_category);

        spinner_category.setSpinnerEventsListener(this);

        adapter = new CategoryAdapter(getContext(), data.getPhraseList());
        spinner_category.setAdapter(adapter);

        mAdapterViewFlipper = v.findViewById(R.id.viewFlipper);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), factImages, factTexts);
        mAdapterViewFlipper.setAdapter(customAdapter);

        mAdapterViewFlipper.setFlipInterval(5000);
        mAdapterViewFlipper.setAutoStart(true);

        dbHelper = new DbHelper(getContext(), "dictionary.db",1);
        try{
            dbHelper.CheckDb();
            dbHelper.OpenDatabase();
        }catch (Exception e){}

        dataList = dbHelper.getWordList();
        wordList = new ArrayList<>(dataList[0]);
        originList = new ArrayList<>(dataList[1]);


//        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, wordList);
//        arrayAdapter.setNotifyOnChange(true);
//        search.setAdapter(arrayAdapter);
//        search.setThreshold(0);


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

                hideKeyboard(view);

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
                }
                return false;
            }
        });


        spinner_category= v.findViewById(R.id.spinner_category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position !=0) {

                hideKeyboard(view);

                Intent intent = new Intent(getContext(),sample_phrases.class);
                ArrayList<Object[]> phraselist = null;

                if (position == data.dialogueIndex) {
                    phraselist = dbHelper.getDialoguelist();
                } else if (position == data.greetingsIndex) {
                    phraselist = dbHelper.getGreetinglist();
                } else if (position == data.directionsIndex) {
                    phraselist = dbHelper.getDirectionsList();
                } else if (position == data.foodIndex) {
                    phraselist = dbHelper.getFoodList();
                } else if (position == data.timeIndex) {
                    phraselist = dbHelper.getTimeList();
                } else if (position == data.familyIndex) {
                    phraselist = dbHelper.getFamilyList();
                } else if (position == data.numbersIndex) {
                    phraselist = dbHelper.getNumbersList();
                } else if (position == data.colorsIndex) {
                    phraselist = dbHelper.getColorsList();
                } else if (position == data.emergencyIndex) {
                    phraselist = dbHelper.getEmergencyList();
                } else if (position == data.sickIndex) {
                    phraselist = dbHelper.getSickList();
                }


                String[] dialoguer = new String[phraselist.size()];
                String[] dialogue_waray = new String[phraselist.size()];
                byte[][] audiofiles = new byte[phraselist.size()][];
                Bundle bun = new Bundle();

                for(int i = 0; i < dialoguer.length; i++) {
                    dialoguer[i] = (String)phraselist.get(i) [0];
                    dialogue_waray[i] = (String)phraselist.get(i) [1];
                    audiofiles[i] = (byte[]) phraselist.get(i) [2];
                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);

                }
                intent.putExtra("englishkey", dialoguer);
                intent.putExtra("waraykey", dialogue_waray);
                intent.putExtra("audiokey", bun);

                startActivity(intent);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });



    spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            hideKeyboard(view);
            filteredOrigin = origin[position];

            search.setHint(filteredOrigin);


            if (filteredOrigin.equalsIgnoreCase(origin[0])) {

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

            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, wordList);
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





//    button_converse.setOnClickListener(new View.OnClickListener()
//    {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(getContext(),sample_phrases.class);
//            ArrayList<Object[]> dialoguelist = dbHelper.getDialoguelist();
//            String[] dialoguer = new String[dialoguelist.size()];
//            String[] dialogue_waray = new String[dialoguelist.size()];
//            byte[][] audiofiles = new byte[dialoguelist.size()][];
//            Bundle bun = new Bundle();
//
//            for(int i = 0; i < dialoguer.length; i++) {
//                dialoguer[i] = (String)dialoguelist.get(i) [0];
//                dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//            }
//            intent.putExtra("englishkey", dialoguer);
//            intent.putExtra("waraykey", dialogue_waray);
//            intent.putExtra("audiokey", bun);
//
//            startActivity(intent);
//        }
//    });
//
//        button_greetings= v.findViewById(R.id.button_greetings);
//        button_greetings.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getGreetinglist();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//        button_directions = v.findViewById(R.id.button_directions);
//        button_directions.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getDirectionsList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//        button_food= v.findViewById(R.id.button_food);
//        button_food.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getFoodList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//        button_time= v.findViewById(R.id.button_time);
//        button_time.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getTimeList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//        button_family= v.findViewById(R.id.button_family);
//        button_family.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getFamilyList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//
//        button_numbers= v.findViewById(R.id.button_numbers);
//        button_numbers.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getNumbersList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//
//        button_colors= v.findViewById(R.id.button_colors);
//        button_colors.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getColorsList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//
//        button_emergency= v.findViewById(R.id.button_emergency);
//        button_emergency.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getEmergencyList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//
//        button_sick= v.findViewById(R.id.button_sick);
//        button_sick.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),sample_phrases.class);
//                ArrayList<Object[]> dialoguelist = dbHelper.getSickList();
//                String[] dialoguer = new String[dialoguelist.size()];
//                String[] dialogue_waray = new String[dialoguelist.size()];
//                byte[][] audiofiles = new byte[dialoguelist.size()][];
//                Bundle bun = new Bundle();
//
//                for(int i = 0; i < dialoguer.length; i++) {
//                    dialoguer[i] = (String)dialoguelist.get(i) [0];
//                    dialogue_waray[i] = (String)dialoguelist.get(i) [1];
//                    audiofiles[i] = (byte[]) dialoguelist.get(i) [2];
//                    bun.putByteArray(dialogue_waray[i], audiofiles[i]);
//
//                }
//                intent.putExtra("englishkey", dialoguer);
//                intent.putExtra("waraykey", dialogue_waray);
//                intent.putExtra("audiokey", bun);
//
//                startActivity(intent);
//            }
//        });
//
//
        return v;
    }


    /***
     *
     * hide keyboard
     */
    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
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
                (String)content[8],
                (String)content[9],
                (String)content[10],



        };
        intent.putExtra("content_key", str_content);
        intent.putExtra("word_key", HomeFragment.word_selected);
        intent.putExtra("audio_key", (byte[])content[6]);
        intent.putExtra("filteredOrigin", filteredOrigin);
        startActivityForResult(intent, REQUEST_CODE_1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (REQUEST_CODE_1 == requestCode && resultCode == RESULT_OK) {
            filteredOrigin = dataIntent.getStringExtra("filteredOrigin");
            search.setHint(filteredOrigin);

            int originIndex = 0;

            for(int index = 0; index < origin.length; index++) {
                if(filteredOrigin.equalsIgnoreCase(origin[index])) {
                    originIndex = index;
                    break;
                }

            }

            spinnerFilter.setSelection(originIndex);
        }
    }

    public static Object[] content = new Object[1];

    public Object[] getAns(String word) {
        content = dbHelper.getAns(word);

        return content;
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        spinner_category.setBackground(getResources().getDrawable(R.drawable.bg_spinner_up));

    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        spinner_category.setBackground(getResources().getDrawable(R.drawable.bg_spinner));


    }

    class CustomAdapter extends BaseAdapter {

        Context context;
        int[] factImages;
        String[] factTexts;
        LayoutInflater inflater;

        public CustomAdapter(Context context, int[] factImages, String[] factTexts) {
            this.context = context;
            this.factImages = factImages;
            this.factTexts = factTexts;
            inflater = (LayoutInflater.from(getContext()));

        }

        @Override
        public int getCount() {
            return factTexts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflater.inflate(R.layout.adapter_layout, null);

            ImageView imageView = view.findViewById(R.id.imagefact);
            TextView textView = view.findViewById(R.id.textfact);

            imageView.setImageResource(factImages[position]);
            textView.setText(factTexts[position]);

            return view;
        }
    }
}