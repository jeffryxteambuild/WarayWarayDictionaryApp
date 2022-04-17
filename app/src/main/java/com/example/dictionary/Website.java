package com.example.dictionary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Website extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        WebView webView = findViewById(R.id.website);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final Context myApp = this;

        /* WebChromeClient must be set BEFORE calling loadUrl! */
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
            {
                new AlertDialog.Builder(myApp)
                        .setTitle("Confirmation")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                (dialog, which) -> result.confirm())
                        .setCancelable(false)
                        .create()
                        .show();

                return true;
            };
        });

        webView.loadUrl("https://waray-waray-dictionary-portal.netlify.app/?fbclid=IwAR3lEQpWT5YT6XU87x09FL8rR3Schke_fYMwQpAC7cVruGqcbjonWk8iMuc");
    }
}