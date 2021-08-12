package com.example.tuptr.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.tuptr.R;

import static com.example.tuptr.Manager.URL_Routes.BASE_URL;


public class ReportActivity extends Activity {
    String URL_SERVER = BASE_URL;
//    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        WebView webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(URL_SERVER);

        //getSettings().setJavaScriptEnabled(true);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
//            this.webView.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

}