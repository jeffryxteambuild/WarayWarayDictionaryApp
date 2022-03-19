package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://forms.gle/btqjNciDpHxVY5Cr8"));

            Intent chooser = Intent.createChooser(browserIntent, "Open via: ");

            if (browserIntent.resolveActivity(getPackageManager()) !=null) {
                startActivity(chooser);
                finish();
            }

//            WebView webView = findViewById(R.id.feedback);
//            webView.setWebViewClient(new WebViewClient());
//            webView.loadUrl("https://forms.gle/btqjNciDpHxVY5Cr8");
    }
}