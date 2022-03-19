package com.example.dictionary.utility;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class AudioHandler {

    private static String path = "/data/data/";

    public static void saveToLocal(byte[] audio, String wordFileName, Context context) throws IOException {

        String audioPath = wordFileName + ".mp3";
        FileOutputStream fileOutputStream = context.openFileOutput(audioPath, Context.MODE_PRIVATE);// = new FileOutputStream(audioFile);
        fileOutputStream.write(audio);
        fileOutputStream.close();
    }


    public static MediaPlayer getmediaPlayer(Context context, String wordFileName) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();

        FileInputStream inputStream = context.openFileInput(wordFileName + ".mp3");

        mediaPlayer.setDataSource(inputStream.getFD());
        inputStream.close();
        mediaPlayer.prepare();


        return mediaPlayer;
    }


}
