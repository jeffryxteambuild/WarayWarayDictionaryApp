package com.example.dictionary.dbdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    String dbName;
    Context context;
    String dbPath;
    ArrayList<String> wordList;

    String tableName = "word_entity";
    String saved_table = "saved_entity";
    String history_table = "history_entity";
    String dialogue_table = "dialogue_phrase";
    String greeting_table = "greetings_phrase";
    String directions_table = "directions_phrase";
    String food_table = "food_phrase";
    String time_table = "time_phrase";
    String family_table = "family_phrase";
    String numbers_table = "numbers_phrase";
    String colors_table = "colors_phrase";
    String emergency_table = "emergency_phrase";
    String sick_table = "sick_phrase";

    String wordCol = "word_search";

    public DbHelper(Context mcontext, String name, int version) {
        super(mcontext, name, null, version);

        this.context = mcontext;
        this.dbName = name;
        this.dbPath = "/data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CheckDb() {
        SQLiteDatabase checkDb = null;

        try {
            String filePath = dbPath + dbName;

            checkDb = SQLiteDatabase.openDatabase(filePath, null, 0);


        } catch (Exception e) {
        }

        if (checkDb != null) {
            Log.d("checkDb", "Database already exists");
            checkDb.close();
        } else {
            CopyDatabase();
        }
    }

    public void CopyDatabase() {
        this.getReadableDatabase();

        try {
            InputStream is = context.getAssets().open(dbName);
            OutputStream os = new FileOutputStream(dbPath + dbName);

            byte[] buffer = new byte[1024 * 8];

            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);

            }
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("CopyDb","Database Copied");

    }

    public void OpenDatabase(){
        String filepath = dbPath + dbName;
        SQLiteDatabase.openDatabase(filepath, null, 0);
    }



    public Object[] getAns(String word) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Object [] content = new Object[1];

        Object xword_search = "",
                xword = "",
                xfos = "",
                xdefinition = "",
                xtranslation = "",
                xexample = "",
                xword_speak = "",
                xcontributor = "",
                xalso = "",
                xdialect = "",
                xorigin = "";

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + tableName + " WHERE word_search = '"+word+"';", null

        );

        if (cursor.moveToNext()) {
            xword_search = cursor.getString(cursor.getColumnIndex("word_search"));
            xword = cursor.getString(cursor.getColumnIndex("word"));
            xfos = cursor.getString(cursor.getColumnIndex("PoS"));
            xdefinition = cursor.getString(cursor.getColumnIndex("definition"));
            xtranslation = cursor.getString(cursor.getColumnIndex("translation"));
            xexample = cursor.getString(cursor.getColumnIndex("example"));
            xword_speak = cursor.getBlob(cursor.getColumnIndex("word_speak"));
            xcontributor = cursor.getString(cursor.getColumnIndex("contributor"));
            xalso = cursor.getString(cursor.getColumnIndex("other_words"));
            xdialect = cursor.getString(cursor.getColumnIndex("dialect"));
            xorigin = cursor.getString(cursor.getColumnIndex("origin"));
            content = new Object[] {xword_search, xword, xfos, xdefinition, xtranslation, xexample, xword_speak, xcontributor, xalso, xdialect, xorigin};
        }

        sqLiteDatabase.close();
        cursor.close();

        return content;
    }

    public ArrayList<String>[] getWordList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String>[] preloaded_data = new ArrayList[2];
        preloaded_data[0] = new ArrayList<>();
        preloaded_data[1] = new ArrayList<>();


        String xword_search = "";
        String xorigin = "";

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT word_search,origin FROM " + tableName, null
        );

        long wordId = 0;

        while (cursor.moveToNext()){
            xword_search = cursor.getString(cursor.getColumnIndex("word_search"));
            xorigin = cursor.getString(cursor.getColumnIndex("origin"));
            preloaded_data[0].add(xword_search);
            preloaded_data[1].add(xorigin == null?String.valueOf(wordId) : xorigin);
            wordId++;

        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_data;
    }

    public ArrayList<String> getSavedList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> preloaded_wordList = new ArrayList<>();
        String xword = "";

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT saved_word FROM " + saved_table, null
        );

        while (cursor.moveToNext()){
            xword = cursor.getString(cursor.getColumnIndex("saved_word"));
            preloaded_wordList.add(xword);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public boolean saved_word(String word) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase.delete(saved_table, "saved_word =?",new String[]{word}) > 0 ) {
            sqLiteDatabase.close();
            return false;
        }
        sqLiteDatabase.close();
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("saved_word", word);
        boolean issaved = sqLiteDatabase.insert(saved_table, null, contentValues) != -1;

        sqLiteDatabase.close();

        return issaved;
    }

    public void clearsaved() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(saved_table, null,null);
        sqLiteDatabase.close();

    }

    public ArrayList<String> getHistoryList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<String> preloaded_wordList = new ArrayList<>();
        String xword = "";

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT history_word FROM " + history_table, null
        );

        while (cursor.moveToNext()){
            xword = cursor.getString(cursor.getColumnIndex("history_word"));
            preloaded_wordList.add(xword);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public boolean history_word(String word) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(history_table, "history_word =?",new String[]{word});
        sqLiteDatabase.close();
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("history_word", word);
        boolean issaved = sqLiteDatabase.insert(history_table, null, contentValues) != -1;

        sqLiteDatabase.close();

        return issaved;
    }

    public void clearhistory() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(history_table, null,null);
        sqLiteDatabase.close();

    }



    //Dialogue table

    public ArrayList<Object[]> getDialoguelist() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String dialogue_english = "";
        String dialogue_waray = "";
        byte []audiofile = null;
        Object[] dialoguer = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + dialogue_table, null
        );

        while (cursor.moveToNext()){
            dialogue_english = cursor.getString(cursor.getColumnIndex("dialogue_english"));
            dialogue_waray = cursor.getString(cursor.getColumnIndex("dialogue_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("dialogue_audio"));
            dialoguer = new Object[] {dialogue_english, dialogue_waray, audiofile};
            preloaded_wordList.add(dialoguer);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }


    public ArrayList<Object[]> getGreetinglist() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String greeting_english = "";
        String greeting_waray = "";
        byte []audiofile = null;
        Object[] greetingr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + greeting_table, null
        );

        while (cursor.moveToNext()){
            greeting_english = cursor.getString(cursor.getColumnIndex("greetings_english"));
            greeting_waray = cursor.getString(cursor.getColumnIndex("greetings_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("greetings_audio"));
            greetingr = new Object[] {greeting_english, greeting_waray, audiofile};
            preloaded_wordList.add(greetingr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getDirectionsList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String directions_english = "";
        String directions_waray = "";
        byte []audiofile = null;
        Object[] directionsr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + directions_table, null
        );

        while (cursor.moveToNext()){
            directions_english = cursor.getString(cursor.getColumnIndex("directions_english"));
            directions_waray = cursor.getString(cursor.getColumnIndex("directions_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("directions_audio"));
            directionsr = new Object[] {directions_english, directions_waray, audiofile};
            preloaded_wordList.add(directionsr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getFoodList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String food_english = "";
        String food_waray = "";
        byte []audiofile = null;
        Object[] foodr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + food_table, null
        );

        while (cursor.moveToNext()){
            food_english = cursor.getString(cursor.getColumnIndex("food_english"));
            food_waray = cursor.getString(cursor.getColumnIndex("food_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("food_audio"));
            foodr = new Object[] {food_english, food_waray, audiofile};
            preloaded_wordList.add(foodr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getTimeList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String time_english = "";
        String time_waray = "";
        byte []audiofile = null;
        Object[] timer = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + time_table, null
        );

        while (cursor.moveToNext()){
            time_english = cursor.getString(cursor.getColumnIndex("time_english"));
            time_waray = cursor.getString(cursor.getColumnIndex("time_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("time_audio"));
            timer = new Object[] {time_english, time_waray, audiofile};
            preloaded_wordList.add(timer);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getFamilyList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String family_english = "";
        String family_waray = "";
        byte []audiofile = null;
        Object[] familyr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + family_table, null
        );

        while (cursor.moveToNext()){
            family_english = cursor.getString(cursor.getColumnIndex("family_english"));
            family_waray = cursor.getString(cursor.getColumnIndex("family_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("family_audio"));
            familyr = new Object[] {family_english, family_waray, audiofile};
            preloaded_wordList.add(familyr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getNumbersList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String numbers_english = "";
        String numbers_waray = "";
        byte []audiofile = null;
        Object[] numbersr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + numbers_table, null
        );

        while (cursor.moveToNext()){
            numbers_english = cursor.getString(cursor.getColumnIndex("numbers_english"));
            numbers_waray = cursor.getString(cursor.getColumnIndex("numbers_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("numbers_audio"));
            numbersr = new Object[] {numbers_english, numbers_waray, audiofile};
            preloaded_wordList.add(numbersr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getColorsList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String colors_english = "";
        String colors_waray = "";
        byte []audiofile = null;
        Object[] colorsr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + colors_table, null
        );

        while (cursor.moveToNext()){
            colors_english = cursor.getString(cursor.getColumnIndex("colors_english"));
            colors_waray = cursor.getString(cursor.getColumnIndex("colors_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("colors_audio"));
            colorsr = new Object[] {colors_english, colors_waray, audiofile};
            preloaded_wordList.add(colorsr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getEmergencyList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String emergency_english = "";
        String emergency_waray = "";
        byte []audiofile = null;
        Object[] emergencyr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + emergency_table, null
        );

        while (cursor.moveToNext()){
            emergency_english = cursor.getString(cursor.getColumnIndex("emergency_english"));
            emergency_waray = cursor.getString(cursor.getColumnIndex("emergency_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("emergency_audio"));
            emergencyr = new Object[] {emergency_english, emergency_waray, audiofile};
            preloaded_wordList.add(emergencyr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }

    public ArrayList<Object[]> getSickList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Object[]> preloaded_wordList = new ArrayList<>();
        String sick_english = "";
        String sick_waray = "";
        byte []audiofile = null;
        Object[] sickr = new Object[0];

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + sick_table, null
        );

        while (cursor.moveToNext()){
            sick_english = cursor.getString(cursor.getColumnIndex("sick_english"));
            sick_waray = cursor.getString(cursor.getColumnIndex("sick_waray"));
            audiofile = cursor.getBlob(cursor.getColumnIndex("sick_audio"));
            sickr = new Object[] {sick_english, sick_waray, audiofile};
            preloaded_wordList.add(sickr);
        }
        sqLiteDatabase.close();
        cursor.close();
        return preloaded_wordList;
    }
}
