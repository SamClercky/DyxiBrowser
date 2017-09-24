package clercky.be.dyxibrowser;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

import clercky.be.dyxibrowser.clients.ChromeClient;
import clercky.be.dyxibrowser.clients.WebClient;

public class MainBrowserActivity extends AppCompatActivity {
    WebViewManager wvm;
    EditText uriEditText;
    Button backBtn;
    Button fwdBtn;
    ProgressBar loadBar;
    String currUrl = "";

    public static boolean canLoadJs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_browser);

        createUI();
    }

    private void createUI() {
        WebView wb = (WebView) findViewById(R.id.webview);
        uriEditText = (EditText) findViewById(R.id.uriEditText);
        backBtn = (Button) findViewById(R.id.backBtn);
        fwdBtn = (Button) findViewById(R.id.fwdBtn);
        loadBar = (ProgressBar) findViewById(R.id.loaddBar);

        wvm = new WebViewManager(this, wb, loadBar);
        wvm.setCallback(new WebViewManager.URLChangedCallback() {
            @Override
            public void onURLChanged(String url) {
                updateUri(url);
            }
        });

        uriEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onGoPage();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvm.goBack();
            }
        });
        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvm.goForward();
            }
        });
    }

    public void onGoPage() {
        wvm.goPage(uriEditText.getText().toString());
    }

    public void updateUri(String uri) {
        enableBtn();
        // update uriEditText
        if (uriEditText == null) return;

        uriEditText.setText(uri);
    }

    public void enableBtn() {
        if (wvm.canGoBack()) {
            backBtn.setEnabled(true);
        } else {
            backBtn.setEnabled(false);
        }
        if (wvm.canGoForward()) {
            fwdBtn.setEnabled(true);
        } else {
            fwdBtn.setEnabled(false);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        if (ev.getAction() == KeyEvent.ACTION_DOWN) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    wvm.goBack();
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    onGoPage();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, ev);
    }
}
