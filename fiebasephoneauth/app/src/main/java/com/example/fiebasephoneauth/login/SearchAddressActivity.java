package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.fiebasephoneauth.R;

public class SearchAddressActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

    webView.loadUrl("https://fir-phoneauth-97f7e.web.app/");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void processDATA(String data) {
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra("address", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}