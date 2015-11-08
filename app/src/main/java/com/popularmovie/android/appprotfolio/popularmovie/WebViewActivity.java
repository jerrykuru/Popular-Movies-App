package com.popularmovie.android.appprotfolio.popularmovie;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
    private static final String LOG_TAG = WebViewActivity.class.getSimpleName();

    private WebView webView;
    private Uri mUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_trailer_web_view);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        String urlConst =  "https://www.youtube.com/watch?v=";
        String youTubeKey = getIntent().getStringExtra(String.valueOf(R.string.youTube_key));
        webView.loadUrl(urlConst+youTubeKey);

    }

}