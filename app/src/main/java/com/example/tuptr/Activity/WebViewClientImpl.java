package com.example.tuptr.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.example.tuptr.Manager.URL_Routes.BASE_URL;

public class WebViewClientImpl extends WebViewClient {
    private Activity activity = null;
    String URL_SERVER = BASE_URL;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(url.indexOf(URL_SERVER) > -1 ) return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }
}
