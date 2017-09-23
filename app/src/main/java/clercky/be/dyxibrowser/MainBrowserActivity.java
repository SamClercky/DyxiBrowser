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

public class MainBrowserActivity extends AppCompatActivity {
    WebView wb;
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

        wb = (WebView) findViewById(R.id.webview);
        uriEditText = (EditText) findViewById(R.id.uriEditText);
        backBtn = (Button) findViewById(R.id.backBtn);
        fwdBtn = (Button) findViewById(R.id.fwdBtn);
        loadBar = (ProgressBar) findViewById(R.id.loaddBar);

        WebSettings webSettings = wb.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                injectScriptFile(view, "styles.css"); // see below ...
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                Log.i("WEB_VIEW_TEST", "error code:");
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                currUrl = url;
                updateUri(url);
                canLoadJs = true;
                // TODO: set favicon
            }
        });
        wb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                if (progress < 100 && loadBar.getVisibility() == ProgressBar.GONE) {
                    loadBar.setVisibility(ProgressBar.VISIBLE);
                }

                loadBar.setProgress(progress);

                if (progress == 100) {
                    loadBar.setVisibility(ProgressBar.GONE);
                }

                if (progress > 25) {
                    injectScriptFile(view, "styles.css");
                }
            }
        });
        goPage("https://www.google.be");

        CookieManager.getInstance().setAcceptCookie(true);

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
                goBack();
            }
        });
        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForward();
            }
        });
    }

    public void onGoPage() {
        goPage(uriEditText.getText().toString());
    }

    public void goPage(String uri) {
        if (currUrl == uri) return;

        if (!uri.startsWith("http"))
            uri = "https://www.google.be/#q=" + uri;

        wb.loadUrl(uri);

        updateUri(uri);
    }
    public void updateUri(String uri) {
        enableBtn();
        // update uriEditText
        if (uriEditText == null) return;

        uriEditText.setText(uri);
    }
    public boolean goBack() {
        if (wb.canGoBack()) {
            wb.goBack();
            return true;
        } else {
            goPage("https://www.google.be");
            return false;
        }
    }
    public boolean goForward() {
        if (wb.canGoForward()) {
            wb.goForward();
            return true;
        } else {
            return false;
        }
    }

    public void enableBtn() {
        if (wb.canGoBack()) {
            backBtn.setEnabled(true);
        } else {
            backBtn.setEnabled(false);
        }
        if (wb.canGoForward()) {
            fwdBtn.setEnabled(true);
        } else {
            fwdBtn.setEnabled(false);
        }
    }

    public void injectScriptFile(WebView view, String scriptFile) {
        if (!canLoadJs) return;

        canLoadJs = false;
        InputStream input;
        try {
            input = getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('style');" +
                    "script.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        if (ev.getAction() == KeyEvent.ACTION_DOWN) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    goBack();
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                    onGoPage();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, ev);
    }
}
