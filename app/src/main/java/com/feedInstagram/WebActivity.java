package com.feedInstagram;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.feedInstagram.utilities.UtilImages;

public class WebActivity extends AppCompatActivity {

    WebView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web);
        container = (WebView) findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        container.getSettings().setJavaScriptEnabled(true);
        container.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                WebActivity.this.setProgress(progress * 1000);
            }

            @Override
            public void onReceivedTitle(WebView view, String sTitle) {
                super.onReceivedTitle(view, sTitle);
                if (sTitle != null && sTitle.length() > 0) {
                    setTitle(sTitle);
                } else {
                    setTitle("Web Page");
                }
            }


        });
        container.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUrl();
    }

    private void loadUrl() {
        String url = getIntent().getExtras().getString(UtilImages.INTENT_URL);
        container.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (container.canGoBack()) {
            container.goBack();
            return;
        }

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
}
