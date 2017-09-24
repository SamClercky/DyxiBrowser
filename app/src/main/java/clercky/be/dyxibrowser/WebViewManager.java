package clercky.be.dyxibrowser;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import clercky.be.dyxibrowser.clients.ChromeClient;
import clercky.be.dyxibrowser.clients.WebClient;

/**
 * Created by Clercky on 24/09/2017.
 */

public class WebViewManager implements UpdateUrl {
    Context ctx;
    WebView wb;
    ProgressBar loadBar;
    String currUrl = "";
    WebViewManager.URLChangedCallback callback;

    public WebViewManager(Context ctx, WebView webView, ProgressBar loadBar) {
        this.ctx = ctx;
        this.wb = webView;
        this.loadBar = loadBar;

        setSettings();
    }

    private void setSettings() {
        WebSettings webSettings = wb.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        wb.setWebViewClient(new WebClient(this));
        wb.setWebChromeClient(new ChromeClient(ctx, loadBar));
        goPage("https://www.google.be");

        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public void updateUri(String url) {
        if (callback != null) callback.onURLChanged(url);
    }

    public void goPage(String uri) {
        //if (currUrl == uri) return;

        if (!uri.startsWith("http"))
            uri = "https://www.google.be/#q=" + uri;

        wb.loadUrl(uri);

        updateUri(uri);
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

    public boolean canGoBack() {
        return wb.canGoBack();
    }

    public boolean goForward() {
        if (wb.canGoForward()) {
            wb.goForward();
            return true;
        } else {
            return false;
        }
    }

    public boolean canGoForward() {
        return wb.canGoForward();
    }

    public void setCallback(WebViewManager.URLChangedCallback callback) {
        this.callback = callback;
    }

    public interface URLChangedCallback {
        public void onURLChanged(String url);
    }
}
